package io.krugosvet.dailydish.android.db.meal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_list_table")
data class MealEntity(
  @PrimaryKey
  val id: Long = 0L,

  var title: String = "",
  var description: String = "",
  var imageUri: String = "",

  @ColumnInfo(name = "last_cooking_date")
  var lastCookingDate: Long = System.currentTimeMillis()
)
