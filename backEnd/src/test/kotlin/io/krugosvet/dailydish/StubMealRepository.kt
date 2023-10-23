package io.krugosvet.dailydish

import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.repository.MealRepository
import kotlinx.coroutines.flow.flowOf

class StubMealRepository : MealRepository {

    lateinit var meals: List<Meal>
    lateinit var resetMeals: List<Meal>

    override fun observe() =
        flowOf(meals)

    override suspend fun add(meal: Meal) {
        meals = listOf(meal)
    }

    override suspend fun delete(mealId: String) {
        meals = meals.minus(get(mealId))
    }

    override suspend fun fetch() {
        TODO("Not yet implemented")
    }

    override suspend fun get(mealId: String): Meal =
        meals.first { it.id == mealId }

    override suspend fun reset() {
        resetMeals
    }

    override suspend fun update(meal: Meal) {
        meals = meals.minus(get(meal.id)).plus(meal)
    }
}
