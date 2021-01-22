package io.krugosvet.dailydish.common.dto

import io.krugosvet.dailydish.common.core.LocalDateSerializer
import io.krugosvet.dailydish.common.core.currentDate
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
public data class AddMealForm(
  val title: String = "",
  val description: String = "",
  // TODO: Find better way to transfer image with MMP in mind
  val image: NewImage? = null,

  @Serializable(LocalDateSerializer::class)
  val date: LocalDate = currentDate,
)

internal class AddMealFormFactory {

  fun from(form: AddMealForm): AddMeal =
    AddMeal(
      form.title,
      form.description,
      null,
      form.date,
    )
}

internal class AddMealFormValidator {

  fun isValid(form: AddMealForm) = with(form) {
    title.isNotBlank()
      && description.isNotBlank()
  }

}
