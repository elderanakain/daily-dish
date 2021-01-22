package io.krugosvet.dailydish.common.repository.db

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class MealDao(
  private val dbQueries: DbQueries
) {

  val meals: List<MealEntity>
    get() = dbQueries.getAll()
      .executeAsList()

  val mealsFlow: Flow<List<MealEntity>>
    get() = dbQueries.getAll()
      .asFlow()
      .mapToList()

  suspend fun get(mealId: String): MealEntity = withContext(Dispatchers.Default) {
    dbQueries.get(mealId)
      .executeAsOne()
  }

  suspend fun replaceAll(mealEntityList: List<MealEntity>) = withContext(Dispatchers.Default) {
      dbQueries.deleteAll()

      mealEntityList.forEach {
        dbQueries.insert(it)
      }
  }

  suspend fun update(mealEntity: MealEntity) = withContext(Dispatchers.Default) {
      with(mealEntity) {
        dbQueries.update(title, description, imageUri, lastCookingDate, id)
      }
  }

  suspend fun add(mealEntity: MealEntity) = withContext(Dispatchers.Default) {
      dbQueries.insert(mealEntity)
  }

  suspend fun delete(mealId: String) = withContext(Dispatchers.Default) {
      dbQueries.delete(mealId)
  }

  suspend fun reset() = withContext(Dispatchers.Default) {
      dbQueries.reset()
  }
}
