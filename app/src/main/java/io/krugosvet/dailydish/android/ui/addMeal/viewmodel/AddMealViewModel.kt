package io.krugosvet.dailydish.android.ui.addMeal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.krugosvet.dailydish.android.architecture.extension.savedStateFlow
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealForm
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisual
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisualFactory
import io.krugosvet.dailydish.android.ui.addMeal.viewmodel.AddMealViewModel.Event
import io.krugosvet.dailydish.android.usecase.AddMealUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class AddMealViewModel(
  savedStateHandle: SavedStateHandle,
  private val visualFactory: AddMealVisualFactory,
  private val addMealUseCase: AddMealUseCase,
) :
  ViewModel<Event>(savedStateHandle) {

  val visual: LiveData<AddMealVisual> by lazy {
    form
      .combine(shouldValidate) { visual, shouldValidate ->
        visualFactory.from(visual, shouldValidate)
      }
      .asLiveData()
  }

  private val form by savedStateFlow(AddMealForm())

  private val shouldValidate = MutableStateFlow(false)

  fun showImagePicker() {
    val event = Event.ShowImagePicker(form.value.image == null)

    navigate(event)
  }

  fun showDatePicker() {
    navigate(Event.ShowDatePicker)
  }

  fun onAddMeal() = viewModelScope.launch {
    addMealUseCase.execute(form.value)
      .onSuccess {
        navigate(Event.Close)
      }
      .onFailure {
        shouldValidate.value = true
      }
  }

  fun onCancel() {
    navigate(Event.Close)
  }

  fun onTitleChange(title: String) {
    form.value = form.value.copy(title = title)
  }

  fun onDescriptionChange(description: String) {
    form.value = form.value.copy(description = description)
  }

  fun onDateChange(date: String) {
    form.value = form.value.copy(date = date)
  }

  fun onImageChange(image: ByteArray?) {
    form.value = form.value.copy(image = image)
  }

  sealed class Event : NavigationEvent() {
    object Close : Event()

    data class ShowImagePicker(
      val isImageEmpty: Boolean
    ) :
      Event()

    object ShowDatePicker : Event()
  }
}
