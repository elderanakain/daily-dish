package io.krugosvet.dailydish.android.ui.addMeal.model

import android.net.Uri
import io.krugosvet.dailydish.android.repository.meal.Meal
import io.krugosvet.dailydish.android.repository.meal.MealFactory
import java.io.Serializable

data class AddMealForm(
  val title: String = "",
  val description: String = "",
  val image: String = "",
  val date: String = "",
) :
  Serializable

fun MealFactory.from(form: AddMealForm): Meal =
  create(form.title, form.description, form.date, Uri.parse(form.image))

class AddMealFormValidator {

  fun isValid(form: AddMealForm) = with(form) {
    title.isNotBlank()
      && description.isNotBlank()
      && image.isNotBlank()
      && date.isNotBlank()
  }

}
