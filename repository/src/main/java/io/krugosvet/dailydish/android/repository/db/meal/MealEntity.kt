package io.krugosvet.dailydish.android.repository.db.meal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_list_table")
data class MealEntity(
  @PrimaryKey
  val id: Long,
  val title: String,
  val description: String,
  val imageUri: String,

  @ColumnInfo(name = "last_cooking_date")
  val lastCookingDate: Long
)
