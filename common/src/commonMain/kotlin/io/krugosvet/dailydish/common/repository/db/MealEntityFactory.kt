package io.krugosvet.dailydish.common.repository.db

import io.krugosvet.dailydish.common.dto.AddMeal
import io.krugosvet.dailydish.common.dto.Meal

internal class MealEntityFactory {

  fun from(meal: Meal) =
    MealEntity(
      id = meal.id,
      title = meal.title,
      description = meal.description,
      imageUri = meal.image,
      lastCookingDate = meal.lastCookingDate.toString()
    )

  fun from(addMeal: AddMeal, newId: String) =
    MealEntity(
      id = newId,
      title = addMeal.title,
      description = addMeal.description,
      imageUri = addMeal.image,
      lastCookingDate = addMeal.lastCookingDate.toString()
    )
}
