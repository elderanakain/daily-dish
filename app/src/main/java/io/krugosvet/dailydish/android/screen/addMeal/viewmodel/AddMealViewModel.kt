package io.krugosvet.dailydish.android.screen.addMeal.viewmodel

import android.app.DatePickerDialog
import android.widget.DatePicker
import io.krugosvet.dailydish.android.architecture.extension.liveData
import io.krugosvet.dailydish.android.architecture.viewmodel.ViewModel
import io.krugosvet.dailydish.android.repository.Meal
import io.krugosvet.dailydish.android.repository.MealRepository
import io.krugosvet.dailydish.android.screen.addMeal.viewmodel.AddMealViewModel.Event
import io.krugosvet.dailydish.android.service.DateService

class AddMealViewModel(
  private val mealRepository: MealRepository,
  private val dateService: DateService
) :
  ViewModel<Event>(),
  DatePickerDialog.OnDateSetListener {

  val title by liveData("")
  val isTitleValid by liveData(false)

  val description by liveData("")
  val isDescriptionValid by liveData(false)

  val date by liveData("")

  val mainImage by liveData("")

  sealed class Event : NavigationEvent() {
    object Close : Event()
    object ShowImagePicker: Event()
  }

  fun showImagePicker() {
    navigate(Event.ShowImagePicker)
  }

  override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
    date.value = dateService.getLongFormattedDate(
      dateService.defaultFormatDate(year, month + 1, dayOfMonth)
    )
  }

  fun onAddMeal() {
    if (!validate()) {
      return
    }

    mealRepository.add(
      Meal(
        title = title.value,
        description = description.value,
        lastCookingDate = dateService.defaultFormatDate(date.value),
        imageUri = mainImage.value
      )
    )
    navigate(Event.Close)
  }

  private fun validate(): Boolean {
    val titleValidation = title.value.isNotEmpty()
    val descriptionValidation = description.value.isNotEmpty()

    isTitleValid.value = titleValidation
    isDescriptionValid.value = descriptionValidation

    return titleValidation && descriptionValidation
  }
}
