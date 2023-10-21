package io.krugosvet.dailydish.common.repository.db

import io.krugosvet.dailydish.common.dto.Meal

internal class MealEntityFactory {

    fun from(meal: Meal) =
        MealEntity(
            id = meal.id,
            title = meal.title,
            description = meal.description,
            lastCookingDate = meal.lastCookingDate.toString(),
        )
}
