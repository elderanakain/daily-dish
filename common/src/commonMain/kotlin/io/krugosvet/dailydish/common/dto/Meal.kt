package io.krugosvet.dailydish.common.dto

import io.krugosvet.dailydish.common.core.LocalDateSerializer
import io.krugosvet.dailydish.common.repository.db.MealEntity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.Serializable

@Serializable
public data class Meal(
    val id: String,

    val title: String,
    val description: String,

    @Serializable(LocalDateSerializer::class)
    val lastCookingDate: LocalDate,
)

internal class MealFactory {

    fun from(mealEntity: MealEntity) =
        Meal(
            id = mealEntity.id,
            title = mealEntity.title,
            description = mealEntity.description,
            lastCookingDate = mealEntity.lastCookingDate.toLocalDate(),
        )
}
