package io.krugosvet.dailydish.android.repository.meal

import android.net.Uri
import io.krugosvet.dailydish.android.repository.db.meal.MealEntity
import io.krugosvet.dailydish.core.service.DateService
import io.krugosvet.dailydish.core.service.IdGenerator
import java.util.*

data class Meal internal constructor(
  val id: Long,
  val title: String,
  val description: String,
  val image: Uri,
  val lastCookingDate: Date
)

class MealFactory(
  private val dateService: DateService,
  private val idGenerator: IdGenerator
) {

  fun create(
    title: String,
    description: String,
    date: String,
    mainImage: Uri
  ) =
    Meal(
      id = idGenerator.generate(),
      title = title,
      description = description,
      lastCookingDate = dateService.defaultFormatDate(date),
      image = mainImage
    )

  fun from(mealEntity: MealEntity) =
    Meal(
      id = mealEntity.id,
      title = mealEntity.title,
      description = mealEntity.description,
      image = Uri.parse(mealEntity.imageUri),
      lastCookingDate = dateService.toDate(mealEntity.lastCookingDate)
    )
}
