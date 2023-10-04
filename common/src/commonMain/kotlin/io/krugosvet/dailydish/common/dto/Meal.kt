package io.krugosvet.dailydish.common.dto

import io.krugosvet.dailydish.common.core.LocalDateSerializer
import io.krugosvet.dailydish.common.repository.db.MealEntity
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.serialization.Serializable

public interface IMeal {
    public val title: String
    public val description: String
    public val image: String?
    public val lastCookingDate: LocalDate

    public fun updateImage(image: String?): IMeal
}

@Serializable
public data class Meal(
    val id: String,

    override val title: String,
    override val description: String,
    override val image: String?,

    @Serializable(LocalDateSerializer::class)
    override val lastCookingDate: LocalDate,
) :
    IMeal {

    override fun updateImage(image: String?): Meal =
        copy(image = image)
}

@Serializable
public data class AddMeal(
    override val title: String,
    override val description: String,
    override val image: String?,

    @Serializable(LocalDateSerializer::class)
    override val lastCookingDate: LocalDate
) :
    IMeal {

    override fun updateImage(image: String?): AddMeal =
        copy(image = image)
}

internal class MealFactory {

    fun from(mealEntity: MealEntity) =
        Meal(
            id = mealEntity.id,
            title = mealEntity.title,
            description = mealEntity.description,
            image = mealEntity.imageUri,
            lastCookingDate = mealEntity.lastCookingDate.toLocalDate()
        )
}
