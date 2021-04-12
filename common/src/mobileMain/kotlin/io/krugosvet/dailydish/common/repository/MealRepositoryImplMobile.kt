package io.krugosvet.dailydish.common.repository

import io.krugosvet.dailydish.common.dto.AddMeal
import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.dto.MealFactory
import io.krugosvet.dailydish.common.dto.NewImage
import io.krugosvet.dailydish.common.repository.db.MealDao
import io.krugosvet.dailydish.common.repository.db.MealEntityFactory
import io.krugosvet.dailydish.common.repository.network.MealService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal actual class MealRepositoryImpl(
    private val mealDao: MealDao,
    private val mealFactory: MealFactory,
    private val mealEntityFactory: MealEntityFactory,
    private val mealService: MealService,
) :
    MealRepository {

    override val meals: List<Meal>
        get() = mealDao.meals
            .map(mealFactory::from)

    override val mealsFlow: Flow<List<Meal>> =
        mealDao.mealsFlow
            .map { mealEntities ->
                mealEntities.map(mealFactory::from)
            }

    override suspend fun add(meal: AddMeal, newImage: NewImage?): String =
        runCatching { mealService.add(meal, newImage) }
            .refreshData()

    override suspend fun update(meal: Meal, newImage: NewImage?) {
        runCatching { mealService.update(meal, newImage) }
            .refreshData()
    }

    override suspend fun delete(mealId: String) {
        runCatching { mealService.delete(mealId) }
            .refreshData()
    }

    override suspend fun get(mealId: String): Meal =
        runCatching { mealService.get(mealId) }
            .getOrThrow()

    override suspend fun fetch() {
        val entities = mealService.getAll()
            .map { mealEntityFactory.from(it) }

        mealDao.replaceAll(entities)
    }

    override suspend fun reset() {
        throw IllegalStateException("Not supported on Android")
    }

    private suspend fun <T> Result<T>.refreshData(): T =
        onSuccess { fetch() }
            .getOrThrow()
}
