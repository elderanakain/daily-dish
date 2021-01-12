package io.krugosvet.dailydish.android.repository.db.meal

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(mealEntityList: List<MealEntity>)

  @Query(value = "select * from meal_list_table where id = :mealId")
  suspend fun get(mealId: String): MealEntity

  @Query(value = "delete from meal_list_table")
  suspend fun clear()

  @Query(value = "select * from meal_list_table")
  fun getAll(): Flow<List<MealEntity>>

  @Query(value = "select * from meal_list_table order by last_cooking_date")
  fun getAllPaged(): PagingSource<Int, MealEntity>
}
