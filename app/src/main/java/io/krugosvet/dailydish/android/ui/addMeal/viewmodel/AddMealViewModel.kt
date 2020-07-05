package io.krugosvet.dailydish.android.ui.addMeal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import io.krugosvet.dailydish.android.architecture.extension.liveData
import io.krugosvet.dailydish.android.architecture.extension.stateLiveData
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.repository.meal.MealFactory
import io.krugosvet.dailydish.android.repository.meal.MealImage
import io.krugosvet.dailydish.android.repository.meal.MealRepository
import io.krugosvet.dailydish.android.ui.addMeal.viewmodel.AddMealViewModel.Event
import io.krugosvet.dailydish.core.service.DateService
import kotlinx.coroutines.launch

class AddMealViewModel(
  savedStateHandle: SavedStateHandle,
  private val mealRepository: MealRepository,
  private val dateService: DateService,
  private val mealFactory: MealFactory
) :
  ViewModel<Event>(savedStateHandle) {

  val title by stateLiveData("")
  val isTitleValid by liveData(false)

  val description by stateLiveData("")
  val isDescriptionValid by liveData(false)

  val date by stateLiveData("")

  val formattedDate: LiveData<String> =
    date.map {
      dateService.getLongFormattedDate(it)
    }

  val mainImage by stateLiveData(MealImage())

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
        mealFactory.create(title.value!!, description.value!!, date.value!!, mainImage.value!!)
      )

      navigate(Event.Close)
    }
  }

  fun onCancel() {
    navigate(Event.Close)
  }

  private fun validate(): Boolean {
    val titleValidation = title.value!!.isNotEmpty()
    val descriptionValidation = description.value!!.isNotEmpty()

    isTitleValid.value = titleValidation
    isDescriptionValid.value = descriptionValidation

    return titleValidation && descriptionValidation
  }
}
