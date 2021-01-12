package io.krugosvet.dailydish.android.usecase

import io.krugosvet.dailydish.android.repository.meal.Meal
import io.krugosvet.dailydish.android.repository.meal.MealFactory
import io.krugosvet.dailydish.android.repository.meal.MealRepository
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealForm
import io.krugosvet.dailydish.android.ui.addMeal.model.AddMealFormValidator
import io.krugosvet.dailydish.android.ui.addMeal.model.from
import io.krugosvet.dailydish.android.usecase.base.IUseCase
import io.krugosvet.dailydish.core.service.DateService

class AddMealUseCase(
  private val repository: MealRepository,
  private val factory: MealFactory,
  private val validator: AddMealFormValidator,
) :
  IUseCase<AddMealForm, Unit> {

  override suspend fun execute(input: AddMealForm): Result<Unit> {
    if (!validator.isValid(input)) {
      return Result.failure(IllegalStateException("Form is not valid"))
    }

    val meal = factory.from(input)

    return runCatching { repository.add(meal, input.image) }
  }
}

class DeleteMealUseCase(
  private val repository: MealRepository,
) :
  IUseCase<Meal, Unit> {

  override suspend fun execute(input: Meal): Result<Unit> = runCatching {
    repository.delete(input)
  }

}

class ChangeMealImageUseCase(
  private val repository: MealRepository,
) :
  IUseCase<ChangeMealImageUseCase.Input, Unit> {

  class Input(
    val meal: Meal,
    val image: ByteArray?,
  )

  override suspend fun execute(input: Input): Result<Unit> = runCatching {
    val meal = when (input.image) {
      null -> input.meal.copy(image = null)
      else -> input.meal
    }

    repository.update(meal, input.image)
  }

}

class SetCurrentTimeToCookedDateMealUseCase(
  private val repository: MealRepository,
  private val dateService: DateService,
) :
  IUseCase<Meal, Unit> {

  override suspend fun execute(input: Meal): Result<Unit> = runCatching {
    val currentDate = dateService.currentDateFormatted
    val updatedMeal = input.copy(lastCookingDate = currentDate)

    repository.update(updatedMeal, null)
  }

}
