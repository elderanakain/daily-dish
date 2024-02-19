package io.krugosvet.dailydish.common.repository

import io.krugosvet.dailydish.common.dto.Meal
import kotlinx.coroutines.flow.Flow

public interface MealRepository {

    public fun observe(): Flow<List<Meal>>

    public suspend fun add(meal: Meal)

    public suspend fun delete(mealId: String)

    public suspend fun reset()

    public suspend fun get(mealId: String): Meal

    public suspend fun update(meal: Meal)

    @Throws(Exception::class)
    public suspend fun fetch()
}

internal expect class MealRepositoryImpl : MealRepository
