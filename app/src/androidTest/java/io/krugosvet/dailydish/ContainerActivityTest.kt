package io.krugosvet.dailydish

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.ui.container.view.ContainerActivity
import io.krugosvet.dailydish.android.ui.mealList.view.MealListAdapter.MealViewHolder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContainerActivityTest {

  @JvmField
  @Rule
  val activityTestRule = ActivityScenarioRule(ContainerActivity::class.java)

  @Test
  fun onShowAddMeal_mealDialogIsOpened() {
    onView(withId(R.id.floatingButton)).apply {
      check(matches(isDisplayed()))
      perform(click())
    }

    onView(withId(R.id.addMealButton))
      .check(matches(isDisplayed()))
  }

  @Test
  fun onClickOnImage_takePictureFromCamera() {
    Espresso.onIdle()

    onView(withId(R.id.mealList))
      .perform(
        actionOnItemAtPosition<MealViewHolder>(0, click())
      )

    onView(withText(R.string.update_picture))
      .check(matches(isDisplayed()))
      .perform(click())

    onView(withText(R.string.take_a_picture))
      .check(matches(isDisplayed()))
      .perform(click())
  }

  @Test
  fun onClickOnImage_takePictureFromGallery() {
    Espresso.onIdle()

    onView(withId(R.id.mealList))
      .perform(
        actionOnItemAtPosition<MealViewHolder>(0, click())
      )

    onView(withText(R.string.update_picture))
      .check(matches(isDisplayed()))
      .perform(click())

    onView(withText(R.string.pick_from_gallery))
      .check(matches(isDisplayed()))
      .perform(click())
  }
}
