package io.krugosvet.dailydish.android.ui.mealList

import android.content.res.Resources
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.OnClick
import io.krugosvet.dailydish.common.core.isToday
import io.krugosvet.dailydish.common.core.toDisplayString
import io.krugosvet.dailydish.common.dto.Meal
import javax.inject.Inject

data class MealVisual(
    val id: String,
    val title: String,
    val description: String,
    val lastDateOfCooking: String,
    val isCookTodayButtonEnabled: Boolean,
    val onDelete: OnClick,
    val onCookTodayClick: OnClick,
)

class MealVisualFactory @Inject constructor(
    private val resources: Resources,
) {

    fun from(
        meal: Meal,
        onDelete: OnClick,
        onCookTodayClick: OnClick,
    ) =
        MealVisual(
            id = meal.id,
            title = meal.title,
            description = meal.description,
            lastDateOfCooking = resources.getString(
                R.string.cooked_on,
                meal.lastCookingDate.toDisplayString(),
            ),
            isCookTodayButtonEnabled = meal.lastCookingDate.isToday,
            onDelete = onDelete,
            onCookTodayClick = onCookTodayClick,
        )
}
