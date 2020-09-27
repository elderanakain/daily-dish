package io.krugosvet.dailydish.android.repository.meal

import io.krugosvet.dailydish.android.repository.db.meal.MealDao
import io.krugosvet.dailydish.android.repository.db.meal.MealEntityFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface IMealRepository {

  val meals: Flow<List<Meal>>

  suspend fun add(meal: Meal)

  suspend fun add(mealList: List<Meal>)

  suspend fun update(meal: Meal)

  suspend fun delete(meal: Meal)
}

class MealRepository(
  private val mealDao: MealDao,
  private val mealFactory: MealFactory,
  private val mealEntityFactory: MealEntityFactory
) :
  IMealRepository {

  override val meals: Flow<List<Meal>> by lazy {
    mealDao.getAll().map { mealEntities ->
      mealEntities.map { mealFactory.from(it) }
    }
  }

  override suspend fun add(meal: Meal) = withContext(Dispatchers.IO) {
    val entity = mealEntityFactory.from(meal)

    mealDao.insert(entity)
  }

  override suspend fun add(mealList: List<Meal>) {
    val entities = mealList.map { mealEntityFactory.from(it) }

    mealDao.insert(entities)
  }

  override suspend fun update(meal: Meal) {
    withContext(Dispatchers.IO) {
      mealEntityFactory.from(meal)
        .also { mealDao.update(it) }
    }
  }

  override suspend fun delete(meal: Meal) = withContext(Dispatchers.IO) {
    mealDao.delete(mealDao.get(meal.id.value))
  }
}
