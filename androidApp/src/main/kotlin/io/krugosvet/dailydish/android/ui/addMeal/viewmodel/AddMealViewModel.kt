package io.krugosvet.dailydish.android.ui.addMeal.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import io.krugosvet.dailydish.android.architecture.extension.savedStateFlow
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisual
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisualFactory
import io.krugosvet.dailydish.android.ui.addMeal.viewmodel.AddMealViewModel.Event
import io.krugosvet.dailydish.common.dto.AddMealForm
import io.krugosvet.dailydish.common.dto.NewImage
import io.krugosvet.dailydish.common.usecase.AddMealUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.datetime.LocalDate
import timber.log.Timber

class AddMealViewModel(
    savedStateHandle: SavedStateHandle,
    private val visualFactory: AddMealVisualFactory,
    private val addMealUseCase: AddMealUseCase,
) :
    ViewModel<Event>(savedStateHandle) {

    private val shouldValidate = MutableStateFlow(false)
    private val form by savedStateFlow(AddMealForm())

    val visual: StateFlow<AddMealVisual?> =
        form
            .combine(shouldValidate) { visual, shouldValidate ->
                visualFactory.from(visual, shouldValidate)
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    fun showImagePicker() {
        val event = Event.ShowImagePicker(form.value.image == null)

        navigate(event)
    }

    fun showDatePicker() {
        navigate(Event.ShowDatePicker)
    }

    fun onAddMeal() = viewModelScope.launchCatching {
        runCatching { addMealUseCase.execute(form.value) }
            .onSuccess {
                navigate(Event.Close)
            }
            .onFailure {
                Timber.e(it)
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

    fun onDateChange(date: LocalDate) {
        form.value = form.value.copy(date = date)
    }

    fun onImageChange(image: NewImage?) {
        form.value = form.value.copy(image = image)
    }

    sealed class Event : NavigationEvent() {
        data object Close : Event()

        data class ShowImagePicker(
            val isImageEmpty: Boolean
        ) :
            Event()

        data object ShowDatePicker : Event()
    }
}
