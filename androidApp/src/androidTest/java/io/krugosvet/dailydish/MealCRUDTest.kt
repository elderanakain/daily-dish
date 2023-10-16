package io.krugosvet.dailydish

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.annotation.IdRes
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.R.drawable
import io.krugosvet.dailydish.base.BaseTest
import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.core.toDisplayString
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// FIXME
//@RunWith(AndroidJUnit4::class)
class MealCRUDTest :
    BaseTest() {

    @JvmField
    @Rule
    val permissions: GrantPermissionRule = GrantPermissionRule.grant(WRITE_EXTERNAL_STORAGE)

    //@Test
    fun whenCreateUpdateDeleteMeal_thenActionsArePropagatedCorrectly() {
        openAddMealScreen()

        fillInMealData()

        addMeal()

        // FIXME Wait for loading
        Thread.sleep(10000)

        findNewMeal()

        updateMealImage()

        deleteMeal()
    }

    @Before
    fun setUp() {
        interceptPhotoGalleryRequest()
    }

    private fun deleteMeal() {
        onView(withId(R.id.mealList))
            .perform(
                actionOnHolderItem(
                    withTitle(MOCK_MEAL_TITLE),
                    clickOnView(R.id.deleteButton)
                )
            )
    }

    private fun updateMealImage() {
        onView(withId(R.id.mealList))
            .perform(
                actionOnHolderItem(
                    withTitle(MOCK_MEAL_TITLE),
                    clickOnView(R.id.meal_image)
                )
            )

        onView(withText(R.string.remove_picture))
            .perform(click())
    }

    private fun findNewMeal() {
        onView(withId(R.id.mealList))
            .perform(scrollToLastPosition())
    }

    private fun addMeal() {
        onView(withId(R.id.addMealButton))
            .perform(
                scrollTo(),
                click()
            )
    }

    private fun fillInMealData() {
        mapOf(
            R.id.title_field to MOCK_MEAL_TITLE,
            R.id.description_field to MOCK_MEAL_DESCRIPTION
        )
            .forEach(::fillInMealField)

        pickCurrentDate()
        takePhoto()
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

    private fun takePhoto() {
        val addMealImage = onView(withId(R.id.addMealImage))

        addMealImage
            .perform(click())

        onView(withText(R.string.pick_from_gallery))
            .perform(click())

        addMealImage
            .check(matches(withDrawable(R.drawable.ic_add_white_24dp)))
    }

    private fun interceptPhotoGalleryRequest() {
        val result = ActivityResult(
            Activity.RESULT_OK,
            Intent().apply {
                data = getResourceUriFrom(drawable.ic_add_white_24dp)
            }
        )

        intending(hasAction(Intent.ACTION_OPEN_DOCUMENT)).respondWith(result)
    }
}

private const val MOCK_MEAL_TITLE = "title"
private const val MOCK_MEAL_DESCRIPTION = "description"
