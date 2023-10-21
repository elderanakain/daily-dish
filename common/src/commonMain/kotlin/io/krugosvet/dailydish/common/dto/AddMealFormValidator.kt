package io.krugosvet.dailydish.common.dto

internal class AddMealFormValidator {

    fun isValid(meal: Meal) = with(meal) {
        title.isNotBlank() &&
            description.isNotBlank()
    }
}
