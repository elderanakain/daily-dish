package io.krugosvet.dailydish.android.ui.addMeal.model

import android.net.Uri
import androidx.annotation.StringRes
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.repository.meal.MealImage
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisual.Date
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisual.Description
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisual.Image
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealVisual.Title
import io.krugosvet.dailydish.core.service.DateService

data class AddMealVisual(
  val title: Title = Title(),
  val description: Description = Description(),
  val image: Image = Image(),
  val date: Date = Date(),
) {

  data class Title(
    val value: String = "",

    @StringRes
    override val error: Int? = null,
  ) :
    ErrorHolder

  data class Description(
    val value: String = "",

    @StringRes
    override val error: Int? = null,
  ) :
    ErrorHolder

  data class Image(
    val value: MealImage = Uri.EMPTY,

    @StringRes
    override val error: Int = R.string.empty,
  ) :
    ErrorHolder

  data class Date(
    val value: String = "",

    @StringRes
    override val error: Int? = null,
  ) :
    ErrorHolder

  private interface ErrorHolder {

    @get:StringRes
    val error: Int?
  }
}

class AddMealVisualFactory(
  private val validator: AddMealVisualValidator,
  private val dateService: DateService,
) {

  fun from(form: AddMealForm, shouldValidate: Boolean): AddMealVisual =
    AddMealVisual(
      title = Title(
        value = form.title,
        error = when {
          !shouldValidate -> null
          !validator.isTitleValid(form.title) -> R.string.incorrect_value
          else -> null
        }
      ),
      description = Description(
        value = form.description,
        error = when {
          !shouldValidate -> null
          !validator.isDescriptionValid(form.description) -> R.string.incorrect_value
          else -> null
        }
      ),
      date = Date(
        value = dateService.getLongFormattedDate(form.date),
        error = when {
          !shouldValidate -> null
          !validator.isDateValid(form.date) -> R.string.incorrect_value
          else -> null
        }
      ),
      image = Image(
        value = Uri.parse(form.image),
        error = when {
          !shouldValidate -> R.string.empty
          !validator.isImageValid(form.image) -> R.string.incorrect_value
          else -> R.string.empty
        }
      )
    )

}

class AddMealVisualValidator {

  fun isTitleValid(title: String): Boolean =
    title.isNotBlank()

  fun isDescriptionValid(description: String): Boolean =
    description.isNotBlank()

  fun isDateValid(date: String): Boolean =
    date.isNotBlank()

  fun isImageValid(image: String): Boolean =
    image.isNotBlank()
}
