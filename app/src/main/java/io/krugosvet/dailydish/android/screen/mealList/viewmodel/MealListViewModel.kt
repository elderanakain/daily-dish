package io.krugosvet.dailydish.android.screen.mealList.viewmodel

import android.net.Uri
import io.krugosvet.dailydish.android.architecture.extension.liveData
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.db.Meal
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisual
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisualFactory
import io.krugosvet.dailydish.android.service.DataBaseService
import io.reactivex.schedulers.Schedulers

class MealListViewModel(
  private val mealVisualFactory: MealVisualFactory,
  private val dataBaseService: DataBaseService
) :
  ViewModel<MealListViewModel.Event>() {

  sealed class Event : NavigationEvent() {
    class ShowImagePicker(val meal: Meal) : Event()
  }

  val mealList by liveData(listOf<MealVisual>())

  init {
    refreshVisual()

    dataBaseService.meals
      .asChangesetObservable()
      .subscribeOn(Schedulers.io())
      .subscribe { refreshVisual() }
      .storeDisposable()
  }

  fun changeImage(meal: Meal, image: Uri) {
    dataBaseService.changeImage(meal, image.toString())

    refreshVisual()
  }

  private fun refreshVisual() =
    mealList.postValue(
      dataBaseService.meals.map { meal ->
        mealVisualFactory.from(
          meal = meal,
          onDelete = { dataBaseService.delete(meal) },
          onImageClick = { navigate(Event.ShowImagePicker(meal)) },
          onCookTodayClick = { dataBaseService.updateDateToCurrent(meal) }
        )
      }
    )
}
