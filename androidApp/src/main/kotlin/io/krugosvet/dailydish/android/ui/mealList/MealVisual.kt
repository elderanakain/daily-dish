package io.krugosvet.dailydish.android.ui.mealList

import android.content.res.Resources
import android.net.Uri
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.extension.OnClick
import io.krugosvet.dailydish.common.core.isToday
import io.krugosvet.dailydish.common.core.toDisplayString
import io.krugosvet.dailydish.common.dto.Meal

data class MealVisual(
    val id: String,
    val title: String,
    val description: String,
    val lastDateOfCooking: String,
    val image: Uri,
    val isCookTodayButtonEnabled: Boolean,
    val onDelete: OnClick,
    val onImageClick: OnClick,
    val onCookTodayClick: OnClick
)

class MealVisualFactory(
    private val resources: Resources,
) {

    fun from(
        meal: Meal,
        onDelete: OnClick,
        onCookTodayClick: OnClick
    ) =
        MealVisual(
            id = meal.id,
            title = meal.title,
            description = meal.description,
            image = if (meal.image == null) Uri.EMPTY else Uri.parse(meal.image),
            lastDateOfCooking = resources.getString(
                R.string.cooked_on, meal.lastCookingDate.toDisplayString()
            ),
            isCookTodayButtonEnabled = meal.lastCookingDate.isToday,
            onDelete = onDelete,
            onImageClick = {},
            onCookTodayClick = onCookTodayClick
        )
}
