package io.krugosvet.dailydish.common.repository

import io.krugosvet.dailydish.common.dto.Meal
import io.krugosvet.dailydish.common.dto.MealFactory
import io.krugosvet.dailydish.common.repository.db.MealDao
import io.krugosvet.dailydish.common.repository.db.MealEntityFactory
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal actual class MealRepositoryImpl(
    private val mealDao: MealDao,
    private val mealFactory: MealFactory,
    private val mealEntityFactory: MealEntityFactory,
) :
    MealRepository {

    override val meals: List<Meal>
        get() = mealDao.meals
            .map(mealFactory::from)

    override val mealsFlow: Flow<List<Meal>>
        get() = mealDao.mealsFlow
            .map {
                it.map(mealFactory::from)
            }

    override suspend fun add(meal: Meal): String {
        val newId = UUID.randomUUID().toString()

        val newEntity = mealEntityFactory.from(meal.copy(id = newId))

        mealDao.add(newEntity)

        return newId
    }

    override suspend fun delete(mealId: String) {
        mealDao.delete(mealId)
    }

    override suspend fun get(mealId: String): Meal {
        val entity = mealDao.get(mealId)

        return mealFactory.from(entity)
    }

    override suspend fun update(meal: Meal) {
        val mealEntity = mealDao.get(meal.id)

        val updatedEntity = mealEntity.copy(
            title = meal.title,
            description = meal.description,
            lastCookingDate = meal.lastCookingDate.toString(),
        )

        mealDao.update(updatedEntity)
    }

    override suspend fun fetch() {}

    override suspend fun reset() {
        mealDao.reset()
    }
}
