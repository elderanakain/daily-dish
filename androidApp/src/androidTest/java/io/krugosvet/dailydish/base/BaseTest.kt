package io.krugosvet.dailydish.base

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions.PositionableRecyclerViewAction
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.ui.container.ContainerActivity
import io.krugosvet.dailydish.android.ui.mealList.MealListAdapter.MealViewHolder
import leakcanary.DetectLeaksAfterTestSuccess
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule

@Suppress("UnnecessaryAbstractClass")
abstract class BaseTest {

    @get:Rule(order = 1)
    val activityRule = IntentsTestRule(ContainerActivity::class.java)

    @get:Rule(order = 2)
    val rule = DetectLeaksAfterTestSuccess()

    protected fun openAddMealScreen() {
        onView(ViewMatchers.withId(R.id.floatingButton)).apply {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            perform(ViewActions.click())
        }
    }

    protected fun withTitle(title: String): Matcher<MealViewHolder> = object :
        BoundedMatcher<MealViewHolder, MealViewHolder>(MealViewHolder::class.java) {

        override fun matchesSafely(item: MealViewHolder): Boolean =
            item.binding.visual!!.title == title

        override fun describeTo(description: Description) {
            description.appendText("view holder with title: $title")
        }
    }

    protected fun clickOnView(@IdRes viewId: Int) =
        object : ViewAction {

            override fun getConstraints() = null

            override fun getDescription(): String = "Click on specific button"

            override fun perform(uiController: UiController, view: View) {
                val button = view.findViewById<View>(viewId)

                button.performClick()
            }
        }

    protected fun scrollToLastPosition() = object : PositionableRecyclerViewAction {

        override fun getConstraints(): Matcher<View> =
            allOf(isAssignableFrom(RecyclerView::class.java), ViewMatchers.isDisplayed())

        override fun getDescription() = ""

        override fun perform(uiController: UiController, view: View) {
            val recyclerView: RecyclerView = view as RecyclerView
            val adapter = recyclerView.adapter!!

            recyclerView.scrollToPosition(adapter.itemCount - 1)
            uiController.loopMainThreadUntilIdle()
        }

        override fun atPosition(position: Int): PositionableRecyclerViewAction {
            throw IllegalStateException("Not supported")
        }
    }
}
