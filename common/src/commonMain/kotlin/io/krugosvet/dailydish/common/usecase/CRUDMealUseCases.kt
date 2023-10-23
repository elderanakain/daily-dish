package io.krugosvet.dailydish.common.usecase

import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.dto.AddMealFormValidator
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.repository.MealRepository

public class AddMealUseCase(
    private val repository: MealRepository,
) {

    private val validator: AddMealFormValidator = AddMealFormValidator()

    public suspend operator fun invoke(meal: Meal): Result<Unit> = runCatching {
        if (!validator.isValid(meal)) {
            error("Form is not valid")
        }

        repository.add(meal)
    }
}

public class DeleteMealUseCase(
    private val repository: MealRepository,
) {

    public suspend operator fun invoke(input: Meal): Result<Unit> = runCatching {
        repository.delete(input.id)
    }
}

public class SetCurrentTimeToCookedDateMealUseCase(
    private val repository: MealRepository,
) {

    public suspend operator fun invoke(input: Meal): Result<Unit> = runCatching {
        val updatedMeal = input.copy(lastCookingDate = currentDate)

        repository.update(updatedMeal)
    }
}
