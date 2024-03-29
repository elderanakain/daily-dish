package io.krugosvet.dailydish.android.ui.addMeal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.krugosvet.dailydish.android.architecture.ViewModel
import io.krugosvet.dailydish.android.ui.addMeal.AddMealViewModel.Event
import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.usecase.AddMealUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import timber.log.Timber

@HiltViewModel
class AddMealViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val visualFactory: AddMealVisualFactory,
    private val addMealUseCase: AddMealUseCase,
) :
    ViewModel<Event>(savedStateHandle) {

    private val shouldValidate = MutableStateFlow(false)

    private val form = MutableStateFlow(
        Meal(id = "", title = "", description = "", lastCookingDate = currentDate),
    )

    val visual: StateFlow<AddMealVisual?> =
        form
            .combine(shouldValidate) { visual, shouldValidate ->
                visualFactory.from(visual, shouldValidate)
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = null)

    fun showDatePicker() {
        navigate(Event.ShowDatePicker)
    }

    fun onAddMeal() = viewModelScope.launchCatching {
        addMealUseCase(form.value)
            .onSuccess { navigate(Event.Close) }
            .onFailure {
                Timber.e(it)
                shouldValidate.value = true
            }
    }

    fun onCancel() {
        navigate(Event.Close)
    }

    fun onTitleChange(title: String) {
        form.update { form.value.copy(title = title) }
    }

    fun onDescriptionChange(description: String) {
        form.update { form.value.copy(description = description) }
    }

    fun onDateChange(date: LocalDate) {
        form.update { form.value.copy(lastCookingDate = date) }
    }

    sealed interface Event : NavigationEvent {

        data object Close : Event

        data object ShowDatePicker : Event
    }
}
