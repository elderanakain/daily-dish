package io.krugosvet.dailydish.android.repository.meal

import android.net.Uri
import io.krugosvet.dailydish.android.repository.db.meal.MealEntity
import io.krugosvet.dailydish.core.service.DateService
import io.krugosvet.dailydish.core.service.IdGenerator
import java.util.*

typealias MealImage = Uri

data class Meal internal constructor(
  val id: MealId,
  val title: String,
  val description: String,
  val image: MealImage,
  val lastCookingDate: Date
)

inline class MealId(
  val value: Long
)

class MealFactory(
  private val dateService: DateService,
  private val idGenerator: IdGenerator
) {

  fun create(
    title: String,
    description: String,
    date: String,
    mainImage: MealImage
  ) =
    Meal(
      id = MealId(idGenerator.generate()),
      title = title,
      description = description,
      lastCookingDate = dateService.defaultFormatDate(date),
      image = mainImage
    )

  fun from(mealEntity: MealEntity) =
    Meal(
      id = MealId(mealEntity.id),
      title = mealEntity.title,
      description = mealEntity.description,
      image = Uri.parse(mealEntity.imageUri),
      lastCookingDate = dateService.toDate(mealEntity.lastCookingDate)
    )
}
