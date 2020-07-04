package io.krugosvet.dailydish

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.ui.container.view.ContainerActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContainerActivityTest {

  @JvmField
  @Rule
  val activityTestRule = ActivityTestRule(ContainerActivity::class.java)

  @Test
  fun onShowAddMeal_mealDialogIsOpened() {
    onView(withId(R.id.floatingButton)).apply {
      check(matches(isDisplayed()))
      perform(ViewActions.click())
    }
    onView(withId(R.id.addMealButton))
      .check(matches(isDisplayed()))
  }
}
