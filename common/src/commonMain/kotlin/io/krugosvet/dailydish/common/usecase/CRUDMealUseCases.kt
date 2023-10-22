package io.krugosvet.dailydish.common.usecase

import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.dto.AddMealFormValidator
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.repository.MealRepository
import io.krugosvet.dailydish.common.usecase.base.IUseCase

public class AddMealUseCase(
    private val repository: MealRepository,
) :
    IUseCase<Meal, Unit> {

    private val validator: AddMealFormValidator = AddMealFormValidator()

    override suspend fun execute(input: Meal) {
        if (!validator.isValid(input)) {
            throw IllegalStateException("Form is not valid")
        }

        repository.add(input)
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

public class SetCurrentTimeToCookedDateMealUseCase(
    private val repository: MealRepository,
) :
    IUseCase<Meal, Unit> {

    override suspend fun execute(input: Meal) {
        val updatedMeal = input.copy(lastCookingDate = currentDate)

        repository.update(updatedMeal)
    }
}
