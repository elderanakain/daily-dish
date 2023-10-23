package io.krugosvet.dailydish.android.ui.addMeal

import androidx.annotation.StringRes
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.ui.addMeal.AddMealVisual.Date
import io.krugosvet.dailydish.android.ui.addMeal.AddMealVisual.Description
import io.krugosvet.dailydish.android.ui.addMeal.AddMealVisual.Title
import io.krugosvet.dailydish.common.core.toDisplayString
import io.krugosvet.dailydish.common.dto.Meal
import kotlinx.datetime.LocalDate

data class AddMealVisual(
    val title: Title = Title(),
    val description: Description = Description(),
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

    fun from(form: Meal, shouldValidate: Boolean): AddMealVisual =
        AddMealVisual(
            title = Title(
                value = form.title,
                error = when {
                    !shouldValidate -> null
                    !validator.isTitleValid(form.title) -> R.string.incorrect_value
                    else -> null
                },
            ),
            description = Description(
                value = form.description,
                error = when {
                    !shouldValidate -> null
                    !validator.isDescriptionValid(form.description) -> R.string.incorrect_value
                    else -> null
                },
            ),
            date = Date(
                value = form.lastCookingDate.toDisplayString(),
                error = when {
                    !shouldValidate -> null
                    !validator.isDateValid(form.lastCookingDate) -> R.string.incorrect_value
                    else -> null
                },
            ),
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
