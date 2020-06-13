package io.krugosvet.dailydish.meal

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MealDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(vararg mealEntity: MealEntity)

  @Update(onConflict = OnConflictStrategy.REPLACE)
  suspend fun update(vararg mealEntity: MealEntity)

  @Delete(entity = MealEntity::class)
  suspend fun delete(vararg mealEntity: MealEntity)

  @Query(value = "select * from meal_list_table where id = :mealId")
  suspend fun get(mealId: Long): MealEntity

  @Query(value = "select * from meal_list_table")
  fun getAll(): Flow<List<MealEntity>>
}
