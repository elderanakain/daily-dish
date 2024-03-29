package io.krugosvet.dailydish.android.ui.mealList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.krugosvet.dailydish.android.architecture.OnClick
import io.krugosvet.dailydish.android.architecture.ViewModel
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationService
import io.krugosvet.dailydish.android.ui.mealList.MealListViewModel.Event
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.repository.MealRepository
import io.krugosvet.dailydish.common.usecase.DeleteMealUseCase
import io.krugosvet.dailydish.common.usecase.SetCurrentTimeToCookedDateMealUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MealListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    mealRepository: MealRepository,
    private val mealVisualFactory: MealVisualFactory,
    private val reminderNotificationService: ReminderNotificationService,
    private val deleteMealUseCase: DeleteMealUseCase,
    private val setCurrentTimeToCookedDateMealUseCase: SetCurrentTimeToCookedDateMealUseCase,
) :
    ViewModel<Event>(savedStateHandle) {

    sealed interface Event : NavigationEvent

    val mealList: StateFlow<List<MealVisual>> =
        mealRepository.observe()
            .map { meals -> meals.map(::mapToVisual) }
            .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = emptyList())

    init {
        viewModelScope.launchCatching {
            mealRepository.fetch()
        }
    }

    private fun onDelete(meal: Meal): OnClick = {
        viewModelScope.launchCatching {
            deleteMealUseCase(meal)
        }
    }

    private fun onCookTodayClick(meal: Meal): OnClick = {
        reminderNotificationService.closeReminder()

        viewModelScope.launchCatching {
            setCurrentTimeToCookedDateMealUseCase(meal)
        }
    }

    private fun mapToVisual(it: Meal): MealVisual =
        mealVisualFactory.from(it, onDelete(it), onCookTodayClick(it))
}
