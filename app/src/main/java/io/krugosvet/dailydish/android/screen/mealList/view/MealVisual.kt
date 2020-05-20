package io.krugosvet.dailydish.android.screen.mealList.view

import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.extension.OnClick
import io.krugosvet.dailydish.android.db.meal.MealEntity
import io.krugosvet.dailydish.android.service.DateService
import io.krugosvet.dailydish.android.service.ResourceService

data class MealVisual(
  val title: String,
  val description: String,
  val lastDateOfCooking: String,
  val image: String,
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
    mealEntity: MealEntity,
    onDelete: OnClick,
    onImageClick: OnClick,
    onCookTodayClick: OnClick
  ) =
    MealVisual(
      title = mealEntity.title,
      description = mealEntity.description,
      image = mealEntity.imageUri,
      lastDateOfCooking = resourceService.getString(
        R.string.cooked_on, dateService.getLongFormattedDate(mealEntity.lastCookingDate)
      ),
      isCookTodayButtonEnabled = !dateService.isCurrentDate(mealEntity.lastCookingDate),
      onDelete = onDelete,
      onImageClick = onImageClick,
      onCookTodayClick = onCookTodayClick
    )
}
