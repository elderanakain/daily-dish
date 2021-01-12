package io.krugosvet.dailydish.android.ui.mealList.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import io.krugosvet.dailydish.android.architecture.extension.OnClick
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationService
import io.krugosvet.dailydish.android.repository.meal.Meal
import io.krugosvet.dailydish.android.repository.meal.MealRepository
import io.krugosvet.dailydish.android.ui.mealList.view.MealVisual
import io.krugosvet.dailydish.android.ui.mealList.view.MealVisualFactory
import io.krugosvet.dailydish.android.usecase.ChangeMealImageUseCase
import io.krugosvet.dailydish.android.usecase.DeleteMealUseCase
import io.krugosvet.dailydish.android.usecase.SetCurrentTimeToCookedDateMealUseCase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

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

    class ShowImagePicker(val meal: Meal) :
      Event()
  }

  val mealList: LiveData<PagingData<MealVisual>> =
    mealRepository.mealsPaged
      .map { paging -> paging.map(::mapToVisual) }
      .cachedIn(viewModelScope)
      .asLiveData()

  init {
    viewModelScope.launch {
      mealRepository.fetch()
    }
  }

  fun changeImage(meal: Meal, image: ByteArray?) = viewModelScope.launch {
    val input = ChangeMealImageUseCase.Input(meal, image)

    changeMealImageUseCase.execute(input)
  }

  fun onPagingStateChange(state: LoadState) {
    val newState = when (state) {
      is LoadState.NotLoading, is LoadState.Error -> State.Inert
      LoadState.Loading -> State.Loading
    }

    setState(newState)
  }

  private fun onDelete(meal: Meal): OnClick = {
    viewModelScope.launch {
      deleteMealUseCase.execute(meal)
    }
  }

  private fun onImageClick(meal: Meal): OnClick = {
    navigate(Event.ShowImagePicker(meal))
  }

  private fun onCookTodayClick(meal: Meal): OnClick = {
    reminderNotificationService.closeReminder()

    viewModelScope.launch {
      setCurrentTimeToCookedDateMealUseCase.execute(meal)
    }
  }

  private fun mapToVisual(it: Meal): MealVisual =
    mealVisualFactory.from(it, onDelete(it), onImageClick(it), onCookTodayClick(it))
}
