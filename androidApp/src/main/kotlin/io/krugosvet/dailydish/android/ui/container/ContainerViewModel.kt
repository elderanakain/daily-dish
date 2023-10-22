package io.krugosvet.dailydish.android.ui.container

import androidx.lifecycle.SavedStateHandle
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.ui.container.ContainerViewModel.Event

class ContainerViewModel(
    savedStateHandle: SavedStateHandle,
) :
    ViewModel<Event>(savedStateHandle) {

    sealed interface Event : NavigationEvent {
        data object ShowAddMeal : Event
    }

    fun onOpenAddMealScreen() {
        navigate(Event.ShowAddMeal)
    }
}
