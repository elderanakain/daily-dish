package io.krugosvet.dailydish

import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.base.BaseTest
import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.core.toDisplayString
import io.krugosvet.dailydish.common.repository.MealRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.component.KoinComponent

@RunWith(AndroidJUnit4::class)
class MealCRUDTest :
    BaseTest(),
    KoinComponent {

    @Test
    fun whenCreateUpdateDeleteMeal_thenActionsArePropagatedCorrectly() = runBlocking {
        getKoin()
            .get<MealRepository>()
            .reset()

        openAddMealScreen()

        fillInMealData()

        addMeal()

        findNewMeal()

        deleteMeal()
    }

    private fun deleteMeal() {
        onView(withId(R.id.mealList))
            .perform(
                actionOnHolderItem(
                    withTitle(MOCK_MEAL_TITLE),
                    clickOnView(R.id.deleteButton),
                ),
            )
    }

    private fun findNewMeal() {
        onView(withId(R.id.mealList))
            .perform(scrollToLastPosition())
    }

    private fun addMeal() {
        onView(withId(R.id.addMealButton))
            .perform(
                scrollTo(),
                click(),
            )
    }

    private fun fillInMealData() {
        mapOf(
            R.id.title_field to MOCK_MEAL_TITLE,
            R.id.description_field to MOCK_MEAL_DESCRIPTION,
        )
            .forEach(::fillInMealField)

        pickCurrentDate()
    }

    private fun fillInMealField(@IdRes field: Int, value: String) {
        onView(withId(field))
            .perform(typeText(value))
            .check(matches(withText(value)))
    }

    private fun pickCurrentDate() {
        val dateField = onView(withId(R.id.date_field))

        dateField
            .perform(click())

        onView(withText("OK"))
            .perform(click())

        dateField
            .check(matches(withText(currentDate.toDisplayString())))
    }
}

private const val MOCK_MEAL_TITLE = "title"
private const val MOCK_MEAL_DESCRIPTION = "description"
