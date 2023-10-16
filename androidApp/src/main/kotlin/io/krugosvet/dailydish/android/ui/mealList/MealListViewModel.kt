package io.krugosvet.dailydish.android.ui.mealList

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import io.krugosvet.dailydish.android.architecture.extension.OnClick
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationService
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.dto.NewImage
import io.krugosvet.dailydish.common.repository.MealRepository
import io.krugosvet.dailydish.common.usecase.ChangeMealImageUseCase
import io.krugosvet.dailydish.common.usecase.DeleteMealUseCase
import io.krugosvet.dailydish.common.usecase.SetCurrentTimeToCookedDateMealUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MealListViewModel(
    savedStateHandle: SavedStateHandle,
    mealRepository: MealRepository,
    private val mealVisualFactory: MealVisualFactory,
    private val reminderNotificationService: ReminderNotificationService,
    private val deleteMealUseCase: DeleteMealUseCase,
    private val changeMealImageUseCase: ChangeMealImageUseCase,
    private val setCurrentTimeToCookedDateMealUseCase: SetCurrentTimeToCookedDateMealUseCase,
) :
    ViewModel<MealListViewModel.Event>(savedStateHandle) {

    sealed class Event :
        NavigationEvent() {

        class ShowImagePicker(val meal: Meal) : Event()
    }

    val mealList: StateFlow<List<MealVisual>> =
        mealRepository.mealsFlow
            .map { meals -> meals.map(::mapToVisual) }
            .stateIn(viewModelScope, SharingStarted.Lazily, initialValue = emptyList())

    init {
        viewModelScope.launchCatching {
            mealRepository.fetch()
        }
    }

    fun changeImage(meal: Meal, image: NewImage?) = viewModelScope.launchCatching {
        val input = ChangeMealImageUseCase.Input(meal, image)

        changeMealImageUseCase.execute(input)
    }

    private fun onDelete(meal: Meal): OnClick = {
        viewModelScope.launchCatching {
            deleteMealUseCase.execute(meal)
        }
    }

    private fun onImageClick(meal: Meal): OnClick = {
        navigate(Event.ShowImagePicker(meal))
    }

    private fun onCookTodayClick(meal: Meal): OnClick = {
        reminderNotificationService.closeReminder()

        viewModelScope.launchCatching {
            setCurrentTimeToCookedDateMealUseCase.execute(meal)
        }
    }

    private fun mapToVisual(it: Meal): MealVisual =
        mealVisualFactory.from(it, onDelete(it), onImageClick(it), onCookTodayClick(it))
}
