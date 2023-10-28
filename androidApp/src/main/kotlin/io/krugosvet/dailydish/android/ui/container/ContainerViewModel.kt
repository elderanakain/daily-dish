package io.krugosvet.dailydish.android.ui.container

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.krugosvet.dailydish.android.architecture.ViewModel
import io.krugosvet.dailydish.android.ui.container.ContainerViewModel.Event
import javax.inject.Inject

@HiltViewModel
class ContainerViewModel @Inject constructor(
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
