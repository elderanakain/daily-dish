package io.krugosvet.dailydish.android.ui.addMeal

import androidx.annotation.StringRes
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.ui.addMeal.AddMealVisual.Date
import io.krugosvet.dailydish.android.ui.addMeal.AddMealVisual.Description
import io.krugosvet.dailydish.android.ui.addMeal.AddMealVisual.Image
import io.krugosvet.dailydish.android.ui.addMeal.AddMealVisual.Title
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
    ) {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Image

            if (value != null) {
                if (other.value == null) return false
                if (!value.contentEquals(other.value)) return false
            } else if (other.value != null) return false

            return true
        }

        override fun hashCode(): Int = value?.contentHashCode() ?: 0
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
            image = getImage(form)
        )

    private fun getImage(form: AddMealForm): Image =
        Image(
            value = form.image?.data,
        )
}

class AddMealVisualValidator {

    fun isTitleValid(title: String): Boolean =
        title.isNotBlank()

    fun isDescriptionValid(description: String): Boolean =
        description.isNotBlank()

    fun isDateValid(date: LocalDate?): Boolean =
        date != null
}
