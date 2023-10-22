package io.krugosvet.dailydish.common.repository

import io.krugosvet.dailydish.common.dto.Meal
import kotlinx.coroutines.flow.Flow

public interface MealRepository {

    public val meals: List<Meal>

    public val mealsFlow: Flow<List<Meal>>

    /**
     * @param meal is a new model to be added
     * @param newImage is optional, if it is present then a new image file will be created
     *
     * @return created [meal] id
     */
    public suspend fun add(meal: Meal): String

    public suspend fun delete(mealId: String)

    public suspend fun reset()

    public suspend fun get(mealId: String): Meal

    public suspend fun update(meal: Meal)

    public suspend fun fetch()
}

internal expect class MealRepositoryImpl : MealRepository
