package io.krugosvet.dailydish.android.repository

import io.krugosvet.dailydish.android.db.meal.MealDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface IMealRepository {

  val meals: Flow<List<Meal>>

  suspend fun add(meal: Meal)

  suspend fun update(meal: Meal)

  suspend fun delete(meal: Meal)
}

class MealRepository(
  private val mealDao: MealDao,
  private val mealFactory: Meal.MealFactory
) :
  IMealRepository {

  override val meals: Flow<List<Meal>>
    get() = mealDao.getAll().map { mealEntities -> mealEntities.map { mealFactory.from(it) } }

  override suspend fun add(meal: Meal) = withContext(Dispatchers.IO) {
    mealDao.insert(mealFactory.toEntity(meal))
  }

  override suspend fun update(meal: Meal) {
    withContext(Dispatchers.IO) {
      mealFactory.toEntity(meal)
        .also { mealDao.update(it) }
    }
  }

  override suspend fun delete(meal: Meal) = withContext(Dispatchers.IO) {
    mealDao.delete(mealDao.get(meal.id))
  }
}
