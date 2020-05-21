package io.krugosvet.dailydish.android.screen.mealList.viewmodel

import android.net.Uri
import io.krugosvet.dailydish.android.architecture.extension.liveData
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.repository.Meal
import io.krugosvet.dailydish.android.repository.MealRepository
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisual
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisualFactory
import io.krugosvet.dailydish.android.service.DateService

class MealListViewModel(
  private val mealVisualFactory: MealVisualFactory,
  private val mealRepository: MealRepository,
  private val dateService: DateService
) :
  ViewModel<MealListViewModel.Event>() {

  sealed class Event : NavigationEvent() {
    class ShowImagePicker(val meal: Meal) : Event()
  }

  val mealList by liveData(listOf<MealVisual>())

  init {
    mealRepository.meals
      .observeForever { meals ->
        refreshVisual(meals)
      }
  }

  fun changeImage(meal: Meal, image: Uri) {
    mealRepository.update(meal.copy(imageUri = image.toString()))
  }

  private fun refreshVisual(meals: List<Meal>) =
    mealList.postValue(
      meals.map { meal ->
        mealVisualFactory.from(
          meal = meal,
          onDelete = {
            mealRepository.delete(meal)
          },
          onImageClick = {
            navigate(Event.ShowImagePicker(meal))
          },
          onCookTodayClick = {
            mealRepository.update(meal.copy(lastCookingDate = dateService.currentDate))
          }
        )
      }
    )
}
