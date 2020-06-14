package io.krugosvet.dailydish.android.screen.addMeal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import io.krugosvet.dailydish.android.architecture.extension.liveData
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.repository.meal.MealFactory
import io.krugosvet.dailydish.android.repository.meal.MealRepository
import io.krugosvet.dailydish.android.screen.addMeal.viewmodel.AddMealViewModel.Event
import io.krugosvet.dailydish.core.service.DateService
import kotlinx.coroutines.launch

class AddMealViewModel(
  private val mealRepository: MealRepository,
  private val dateService: DateService,
  private val mealFactory: MealFactory
) :
  ViewModel<Event>() {

  val title by liveData("")
  val isTitleValid by liveData(false)

  val description by liveData("")
  val isDescriptionValid by liveData(false)

  val date by liveData("")

  val formattedDate: LiveData<String> =
    date.map {
      dateService.getLongFormattedDate(it)
    }

  val mainImage by liveData("")

  sealed class Event : NavigationEvent() {
    object Close : Event()
    object ShowImagePicker : Event()
    object ShowDatePicker : Event()
  }

  fun showImagePicker() {
    navigate(Event.ShowImagePicker)
  }

  fun showDatePicker() {
    navigate(Event.ShowDatePicker)
  }

  fun onAddMeal() {
    if (!validate()) {
      return
    }

    viewModelScope.launch {
      mealRepository.add(
        mealFactory.create(title.value, description.value, date.value, mainImage.value)
      )
    }

    navigate(Event.Close)
  }

  private fun validate(): Boolean {
    val titleValidation = title.value.isNotEmpty()
    val descriptionValidation = description.value.isNotEmpty()

    isTitleValid.value = titleValidation
    isDescriptionValid.value = descriptionValidation

    return titleValidation && descriptionValidation
  }
}
