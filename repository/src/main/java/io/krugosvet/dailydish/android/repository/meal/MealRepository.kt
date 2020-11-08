package io.krugosvet.dailydish.android.repository.meal

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import io.krugosvet.dailydish.android.repository.db.meal.MealDao
import io.krugosvet.dailydish.android.repository.db.meal.MealEntity
import io.krugosvet.dailydish.android.repository.db.meal.MealEntityFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MealRepository(
  private val mealDao: MealDao,
  private val mealFactory: MealFactory,
  private val mealEntityFactory: MealEntityFactory
) {

  val meals: Flow<List<Meal>> by lazy {
    mealDao.getAll().map { mealEntities ->
      mealEntities.map(mealFactory::from)
    }
  }

  val mealsPaged: Flow<PagingData<Meal>> by lazy {
    Pager(PagingConfig(PAGE_SIZE)) { mealDao.getAllPaged() }
      .flow
      .map(::mapToLogic)
  }

  suspend fun add(meal: Meal): Unit = withContext(Dispatchers.IO) {
    val entity = mealEntityFactory.from(meal)

    mealDao.insert(entity)
  }

  suspend fun add(mealList: List<Meal>): Unit = withContext(Dispatchers.IO) {
    val entities = mealList.map { mealEntityFactory.from(it) }

    mealDao.insert(entities)
  }

  suspend fun update(meal: Meal): Unit = withContext(Dispatchers.IO) {
    val entity = mealEntityFactory.from(meal)

    mealDao.update(entity)
  }

  suspend fun delete(meal: Meal): Unit = withContext(Dispatchers.IO) {
    mealDao.delete(mealDao.get(meal.id.value))
  }

  private fun mapToLogic(it: PagingData<MealEntity>): PagingData<Meal> =
    it.map { entity -> mealFactory.from(entity) }

  private companion object {
    const val PAGE_SIZE = 5
  }
}
