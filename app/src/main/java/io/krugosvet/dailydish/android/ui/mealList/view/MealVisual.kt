package io.krugosvet.dailydish.android.ui.mealList.view

import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.extension.OnClick
import io.krugosvet.dailydish.android.repository.meal.Meal
import io.krugosvet.dailydish.android.repository.meal.MealId
import io.krugosvet.dailydish.android.repository.meal.MealImage
import io.krugosvet.dailydish.core.service.DateService
import io.krugosvet.dailydish.core.service.ResourceService

data class MealVisual(
  val id: MealId,
  val title: String,
  val description: String,
  val lastDateOfCooking: String,
  val image: MealImage,
  val isCookTodayButtonEnabled: Boolean,
  val onDelete: OnClick,
  val onImageClick: OnClick,
  val onCookTodayClick: OnClick
)

class MealVisualFactory(
  private val resourceService: ResourceService,
  private val dateService: DateService
) {

  fun from(
    meal: Meal,
    onDelete: OnClick,
    onImageClick: OnClick,
    onCookTodayClick: OnClick
  ) =
    MealVisual(
      id = meal.id,
      title = meal.title,
      description = meal.description,
      image = meal.image,
      lastDateOfCooking = resourceService.getString(
        R.string.cooked_on, dateService.getLongFormattedDate(meal.lastCookingDate)
      ),
      isCookTodayButtonEnabled = !dateService.isCurrentDate(meal.lastCookingDate),
      onDelete = onDelete,
      onImageClick = onImageClick,
      onCookTodayClick = onCookTodayClick
    )
}
