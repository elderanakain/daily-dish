package io.krugosvet.dailydish.android.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.krugosvet.dailydish.android.db.meal.MealDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface IMealRepository {

  val meals: LiveData<List<Meal>>

  suspend fun add(meal: Meal)

  suspend fun update(meal: Meal)

  suspend fun delete(meal: Meal)
}

class MealRepository(
  private val mealDao: MealDao,
  private val mealFactory: MealFactory
) :
  IMealRepository {

  override val meals: LiveData<List<Meal>> =
    Transformations.map(mealDao.getAll()) { mealEntities ->
      mealEntities.map { mealFactory.from(it) }
    }

  override suspend fun add(meal: Meal) = withContext(Dispatchers.IO) {
    mealDao.insert(mealFactory.to(meal))
  }

  override suspend fun update(meal: Meal) = withContext(Dispatchers.IO) {
    mealDao.update(mealFactory.to(meal))
  }

  override suspend fun delete(meal: Meal) = withContext(Dispatchers.IO) {
    mealDao.delete(mealFactory.to(meal))
  }
}
