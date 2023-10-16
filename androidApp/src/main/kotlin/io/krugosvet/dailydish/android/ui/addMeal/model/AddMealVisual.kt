package io.krugosvet.dailydish.android.ui.addMeal.model

import androidx.annotation.StringRes
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisual.Date
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisual.Description
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisual.Image
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisual.Title
import io.krugosvet.dailydish.common.core.toDisplayString
import io.krugosvet.dailydish.common.dto.AddMealForm
import io.krugosvet.dailydish.common.dto.NewImage
import kotlinx.datetime.LocalDate

data class AddMealVisual(
    val title: Title = Title(),
    val description: Description = Description(),
    val image: Image = Image(),
    val date: Date = Date(),
) {

    data class Title(
        val value: String = "",

        @StringRes
        override val error: Int? = null,
    ) :
        ErrorHolder

    data class Description(
        val value: String = "",

        @StringRes
        override val error: Int? = null,
    ) :
        ErrorHolder

    data class Image(
        val value: ByteArray? = null,

        @StringRes
        override val error: Int = R.string.empty,
    ) :
        ErrorHolder {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Image

            if (value != null) {
                if (other.value == null) return false
                if (!value.contentEquals(other.value)) return false
            } else if (other.value != null) return false
            if (error != other.error) return false

            return true
        }

        override fun hashCode(): Int {
            var result = value?.contentHashCode() ?: 0
            result = 31 * result + error
            return result
        }
    }

    data class Date(
        val value: String = "",

        @StringRes
        override val error: Int? = null,
    ) :
        ErrorHolder

    private interface ErrorHolder {

        @get:StringRes
        val error: Int?
    }
}

class AddMealVisualFactory(
    private val validator: AddMealVisualValidator,
) {

    fun from(form: AddMealForm, shouldValidate: Boolean): AddMealVisual =
        AddMealVisual(
            title = Title(
                value = form.title,
                error = when {
                    !shouldValidate -> null
                    !validator.isTitleValid(form.title) -> R.string.incorrect_value
                    else -> null
                }
            ),
            description = Description(
                value = form.description,
                error = when {
                    !shouldValidate -> null
                    !validator.isDescriptionValid(form.description) -> R.string.incorrect_value
                    else -> null
                }
            ),
            date = Date(
                value = form.date.toDisplayString(),
                error = when {
                    !shouldValidate -> null
                    !validator.isDateValid(form.date) -> R.string.incorrect_value
                    else -> null
                }
            ),
            image = getImage(form, shouldValidate)
        )

    private fun getImage(form: AddMealForm, shouldValidate: Boolean): Image =
        Image(
            value = form.image?.data,
            error = when {
                !shouldValidate -> R.string.empty
                !validator.isImageValid(form.image) -> R.string.incorrect_value
                else -> R.string.empty
            }
        )
}

class AddMealVisualValidator {

    fun isTitleValid(title: String): Boolean =
        title.isNotBlank()

    fun isDescriptionValid(description: String): Boolean =
        description.isNotBlank()

    fun isDateValid(date: LocalDate?): Boolean =
        date != null

    fun isImageValid(image: NewImage?): Boolean =
        image != null
}
