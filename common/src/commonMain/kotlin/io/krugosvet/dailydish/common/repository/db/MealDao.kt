package io.krugosvet.dailydish.common.repository.db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

internal class MealDao(
    private val dbQueries: DbQueries,
) {

    val mealsFlow: Flow<List<MealEntity>>
        get() = dbQueries.getAll()
            .asFlow()
            .mapToList(Dispatchers.IO)

    suspend fun get(mealId: String): MealEntity = withContext(Dispatchers.IO) {
        dbQueries.get(mealId)
            .executeAsOne()
    }

    suspend fun replaceAll(mealEntityList: List<MealEntity>) = withContext(Dispatchers.IO) {
        dbQueries.deleteAll()

        mealEntityList.forEach(dbQueries::insert)
    }

    suspend fun update(mealEntity: MealEntity) = withContext(Dispatchers.IO) {
        with(mealEntity) {
            dbQueries.update(title, description, lastCookingDate, id)
        }
    }

    suspend fun add(mealEntity: MealEntity) = withContext(Dispatchers.IO) {
        dbQueries.insert(mealEntity)
    }

    suspend fun delete(mealId: String) = withContext(Dispatchers.IO) {
        dbQueries.delete(mealId)
    }

    suspend fun reset() = withContext(Dispatchers.IO) {
        dbQueries.reset()
    }
}
