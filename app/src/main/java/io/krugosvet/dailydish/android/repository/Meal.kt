package io.krugosvet.dailydish.android.repository

import io.krugosvet.dailydish.android.db.meal.MealEntity
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisual
import io.krugosvet.dailydish.android.service.DateService
import java.util.*

data class Meal(
  val id: Long,
  val title: String,
  val description: String,
  val imageUri: String,
  val lastCookingDate: Date
)

class MealFactory(
  private val dateService: DateService
) {

  fun from(mealVisual: MealVisual) =
    Meal(
      title = mealVisual.title,
      description = mealVisual.description,
      imageUri = mealVisual.image,
      lastCookingDate = dateService.defaultFormatDate(mealVisual.lastDateOfCooking)
    )

  fun from(mealEntity: MealEntity) =
    Meal(
      title = mealEntity.title,
      description = mealEntity.description,
      imageUri = mealEntity.imageUri,
      lastCookingDate = dateService.toDate(mealEntity.lastCookingDate)
    )

  fun to(meal: Meal) =
    MealEntity(
      title = meal.title,
      description = meal.description,
      imageUri = meal.imageUri,
      lastCookingDate = meal.lastCookingDate.time
    )
}
