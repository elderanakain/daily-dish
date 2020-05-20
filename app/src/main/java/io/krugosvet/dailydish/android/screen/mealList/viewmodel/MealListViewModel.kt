package io.krugosvet.dailydish.android.screen.mealList.viewmodel

import android.net.Uri
import io.krugosvet.dailydish.android.architecture.extension.liveData
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.db.meal.MealEntity
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisual
import io.krugosvet.dailydish.android.screen.mealList.view.MealVisualFactory
import io.reactivex.schedulers.Schedulers

class MealListViewModel(
  private val mealVisualFactory: MealVisualFactory,
  private val dataBaseService: DataBaseService
) :
  ViewModel<MealListViewModel.Event>() {

  sealed class Event : NavigationEvent() {
    class ShowImagePicker(val mealEntity: MealEntity) : Event()
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

  fun changeImage(mealEntity: MealEntity, image: Uri) {
    dataBaseService.changeImage(mealEntity, image.toString())

    refreshVisual()
  }

  private fun refreshVisual() =
    mealList.postValue(
      dataBaseService.meals.map { meal ->
        mealVisualFactory.from(
          mealEntity = meal,
          onDelete = { dataBaseService.delete(meal) },
          onImageClick = { navigate(Event.ShowImagePicker(meal)) },
          onCookTodayClick = { dataBaseService.updateDateToCurrent(meal) }
        )
      }
    )
}
