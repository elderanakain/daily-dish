package io.krugosvet.dailydish.android.repository.meal

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import io.krugosvet.dailydish.android.repository.db.meal.MealDao
import io.krugosvet.dailydish.android.repository.db.meal.MealEntity
import io.krugosvet.dailydish.android.repository.db.meal.MealEntityFactory
import io.krugosvet.dailydish.android.repository.network.MealService
import io.krugosvet.dailydish.core.logError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MealRepository(
  private val mealDao: MealDao,
  private val mealFactory: MealFactory,
  private val mealEntityFactory: MealEntityFactory,
  private val mealService: MealService,
  private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
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
      .flowOn(ioDispatcher)
  }

  suspend fun add(meal: AddMeal, newImage: ByteArray?): Unit = withContext(Dispatchers.IO) {
    runCatching { mealService.add(meal, newImage) }
      .refreshData()
  }

  suspend fun update(meal: Meal, newImage: ByteArray?): Unit = withContext(Dispatchers.IO) {
    runCatching { mealService.update(meal, newImage) }
      .refreshData()
  }

  suspend fun delete(meal: Meal) {
    runCatching { mealService.delete(meal) }
      .refreshData()
  }

  suspend fun fetch() = withContext(Dispatchers.IO) {
    val entities = mealService.getAll()
      .map { mealEntityFactory.from(it) }

    mealDao.clear()
    mealDao.insert(entities)
  }

  private fun mapToLogic(it: PagingData<MealEntity>): PagingData<Meal> =
    it.map { entity -> mealFactory.from(entity) }

  private suspend fun <T> Result<T>.refreshData() =
    onSuccess { fetch() }
      .logError()

  private companion object {
    const val PAGE_SIZE = 5
  }
}
