package io.krugosvet.dailydish.common.repository

import io.krugosvet.dailydish.common.dto.Meal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

public interface MealRepository {

    // @NativeCoroutines
    public fun observe(): Flow<List<Meal>>

    fun flow() = flowOf(1, 2, 3)

    // @NativeCoroutines
    public suspend fun add(meal: Meal)

    // @NativeCoroutines
    public suspend fun delete(mealId: String)

    // @NativeCoroutines
    public suspend fun reset()

    // @NativeCoroutines
    public suspend fun get(mealId: String): Meal

    // @NativeCoroutines
    public suspend fun update(meal: Meal)

    // @NativeCoroutines
    public suspend fun fetch()
}

internal expect class MealRepositoryImpl : MealRepository
