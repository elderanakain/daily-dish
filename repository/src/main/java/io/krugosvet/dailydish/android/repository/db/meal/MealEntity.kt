package io.krugosvet.dailydish.android.repository.db.meal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.krugosvet.dailydish.android.repository.meal.Meal

@Entity(tableName = "meal_list_table")
class MealEntity(
  @PrimaryKey(autoGenerate = true)
  val id: Long,
  val title: String,
  val description: String,
  val imageUri: String,

  @ColumnInfo(name = "last_cooking_date")
  val lastCookingDate: Long
)

class MealEntityFactory {

  fun from(meal: Meal) =
    MealEntity(
      id = meal.id.value,
      title = meal.title,
      description = meal.description,
      imageUri = meal.image.uri.toString(),
      lastCookingDate = meal.lastCookingDate.time
    )

}
