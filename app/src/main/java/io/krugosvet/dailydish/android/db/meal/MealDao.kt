package io.krugosvet.dailydish.android.db.meal

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MealDao {

  @Insert
  fun insert(mealEntity: MealEntity)

  @Update
  fun update(mealEntity: MealEntity)

  @Delete
  fun delete(mealEntity: MealEntity)

  @Query(value = "SELECT * from meal_list_table WHERE id = :mealId")
  fun get(mealId: Long): MealEntity

  @Query(value = "SELECT * from meal_list_table")
  fun getAll(): LiveData<List<MealEntity>>
}
