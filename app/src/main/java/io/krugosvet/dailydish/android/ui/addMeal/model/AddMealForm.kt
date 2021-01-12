package io.krugosvet.dailydish.android.ui.addMeal.model

import io.krugosvet.dailydish.android.repository.meal.AddMeal
import io.krugosvet.dailydish.android.repository.meal.MealFactory
import java.io.Serializable

data class AddMealForm(
  val title: String = "",
  val description: String = "",
  // TODO: Find better way to transfer image with MMP in mind
  val image: ByteArray? = null,
  val date: String = "",
) :
  Serializable {

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as AddMealForm

    if (title != other.title) return false
    if (description != other.description) return false
    if (image != null) {
      if (other.image == null) return false
      if (!image.contentEquals(other.image)) return false
    } else if (other.image != null) return false
    if (date != other.date) return false

    return true
  }

  override fun hashCode(): Int {
    var result = title.hashCode()
    result = 31 * result + description.hashCode()
    result = 31 * result + (image?.contentHashCode() ?: 0)
    result = 31 * result + date.hashCode()
    return result
  }
}

fun MealFactory.from(form: AddMealForm): AddMeal =
  create(form.title, form.description, form.date, null)

class AddMealFormValidator {

  fun isValid(form: AddMealForm) = with(form) {
    title.isNotBlank()
      && description.isNotBlank()
      && date.isNotBlank()
  }

}
