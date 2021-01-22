package io.krugosvet.dailydish.common.usecase

import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.dto.AddMealForm
import io.krugosvet.dailydish.common.dto.AddMealFormFactory
import io.krugosvet.dailydish.common.dto.AddMealFormValidator
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.dto.NewImage
import io.krugosvet.dailydish.common.repository.MealRepository
import io.krugosvet.dailydish.common.usecase.ChangeMealImageUseCase.Input
import io.krugosvet.dailydish.common.usecase.base.IUseCase

public class AddMealUseCase(
  private val repository: MealRepository
) :
  IUseCase<AddMealForm, Unit> {

  private val validator: AddMealFormValidator = AddMealFormValidator()
  private val factory: AddMealFormFactory = AddMealFormFactory()

  override suspend fun execute(input: AddMealForm) {
    if (!validator.isValid(input)) {
      throw IllegalStateException("Form is not valid")
    }

    val meal = factory.from(input)

    repository.add(meal, input.image)
  }
}

public class DeleteMealUseCase(
  private val repository: MealRepository,
) :
  IUseCase<Meal, Unit> {

  override suspend fun execute(input: Meal) {
    repository.delete(input.id)
  }

}

public class ChangeMealImageUseCase(
  private val repository: MealRepository,
) :
  IUseCase<Input, Unit> {

  public data class Input(
    public val meal: Meal,
    public val image: NewImage?,
  )

  override suspend fun execute(input: Input) {
    val meal = when (input.image) {
      null -> input.meal.copy(image = null)
      else -> input.meal
    }

    repository.update(meal, input.image)
  }

}

public class SetCurrentTimeToCookedDateMealUseCase(
  private val repository: MealRepository,
) :
  IUseCase<Meal, Unit> {

  override suspend fun execute(input: Meal) {
    val updatedMeal = input.copy(lastCookingDate = currentDate)

    repository.update(updatedMeal, null)
  }

}
