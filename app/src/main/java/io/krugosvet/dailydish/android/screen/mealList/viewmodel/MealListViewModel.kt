package io.krugosvet.dailydish.android.screen.mealList.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.krugosvet.dailydish.android.architecture.extension.OnClick
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.repository.Meal
import io.krugosvet.dailydish.android.repository.MealRepository
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisual
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisualFactory
import io.krugosvet.dailydish.android.service.DateService
import kotlinx.coroutines.flow.map
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

  val mealList: LiveData<List<MealVisual>> =
    mealRepository.meals
      .map { meals ->
        meals.map {
          mealVisualFactory.from(it, onDelete(it), onImageClick(it), onCookTodayClick(it))
        }
      }
      .asLiveData()

  fun changeImage(meal: Meal, image: Uri) {
    viewModelScope.launch {
      mealRepository.update(meal.copy(imageUri = image.toString()))
    }
  }

  private fun onDelete(meal: Meal): OnClick = {
    viewModelScope.launch {
      mealRepository.delete(meal)
    }
  }

  private fun onImageClick(meal: Meal): OnClick = {
    viewModelScope.launch {
      navigate(Event.ShowImagePicker(meal))
    }
  }

  private fun onCookTodayClick(meal: Meal): OnClick = {
    viewModelScope.launch {
      mealRepository.update(meal.copy(lastCookingDate = dateService.currentDate.time))
    }
  }
}
