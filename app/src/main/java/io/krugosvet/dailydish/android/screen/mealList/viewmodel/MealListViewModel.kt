package io.krugosvet.dailydish.android.screen.mealList.viewmodel

import android.net.Uri
import androidx.lifecycle.viewModelScope
import io.krugosvet.dailydish.android.architecture.extension.liveData
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.repository.Meal
import io.krugosvet.dailydish.android.repository.MealRepository
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisual
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisualFactory
import io.krugosvet.dailydish.android.service.DateService
import kotlinx.coroutines.launch

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
    viewModelScope.launch {
      mealRepository.update(meal.copy(imageUri = image.toString()))
    }
  }

  private fun refreshVisual(meals: List<Meal>) =
    mealList.postValue(
      meals.map { meal ->
        mealVisualFactory.from(
          meal = meal,
          onDelete = {
            viewModelScope.launch {
              mealRepository.delete(meal)
            }
          },
          onImageClick = {
            viewModelScope.launch {
              navigate(Event.ShowImagePicker(meal))
            }
          },
          onCookTodayClick = {
            viewModelScope.launch {
              mealRepository.update(meal.copy(lastCookingDate = dateService.currentDate))
            }
          }
        )
      }
    )
}
