package io.krugosvet.dailydish.common.repository

import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.dto.MealFactory
import io.krugosvet.dailydish.common.repository.db.MealDao
import io.krugosvet.dailydish.common.repository.db.MealEntityFactory
import io.krugosvet.dailydish.common.repository.network.MealService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal actual class MealRepositoryImpl(
    private val mealDao: MealDao,
    private val factory: MealFactory,
    private val entityFactory: MealEntityFactory,
    private val mealService: MealService,
) :
    MealRepository {

    override val mealsFlow: Flow<List<Meal>> =
        mealDao.mealsFlow
            .map { entities -> entities.map(factory::from) }

    override suspend fun add(meal: Meal) {
        val entity = entityFactory.from(meal)

        mealDao.add(entity)

        runCatching {
            mealService.add(meal)
            fetch()
        }
    }

    override suspend fun update(meal: Meal) {
        mealService.update(meal)
        fetch()
    }

    override suspend fun delete(mealId: String) {
        mealService.delete(mealId)
        fetch()
    }

    override suspend fun get(mealId: String): Meal =
        mealService.get(mealId)

    override suspend fun fetch() {
        val entities = mealService.getAll()
            .map { entityFactory.from(it) }

        mealDao.replaceAll(entities)
    }

    override suspend fun reset() {
        mealDao.reset()
    }
}
