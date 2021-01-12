package io.krugosvet.dailydish.android.repository.meal

import io.krugosvet.dailydish.android.repository.db.meal.MealEntity
import kotlinx.serialization.Serializable

interface IMeal {
  val title: String
  val description: String
  val image: String?
  val lastCookingDate: String
}

@Serializable
data class Meal(
  val id: String,
  override val title: String,
  override val description: String,
  override val image: String?,
  override val lastCookingDate: String,
) :
  IMeal

@Serializable
data class AddMeal(
  override val title: String,
  override val description: String,
  override val image: String?,
  override val lastCookingDate: String
) :
  IMeal

class MealFactory {

  fun create(
    title: String,
    description: String,
    date: String,
    mainImage: String?
  ) =
    AddMeal(
      title = title,
      description = description,
      lastCookingDate = date,
      image = mainImage
    )

  fun from(mealEntity: MealEntity) =
    Meal(
      id = mealEntity.id,
      title = mealEntity.title,
      description = mealEntity.description,
      image = mealEntity.imageUri,
      lastCookingDate = mealEntity.lastCookingDate
    )
}
