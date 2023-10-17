package io.krugosvet.dailydish.base

import android.content.ContentResolver
import android.content.res.Resources
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.graphics.drawable.toBitmap
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
import androidx.test.platform.app.InstrumentationRegistry
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.ui.container.ContainerActivity
import io.krugosvet.dailydish.android.ui.mealList.MealListAdapter.MealViewHolder
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule

@Suppress("UnnecessaryAbstractClass")
abstract class BaseTest {

    @JvmField
    @Rule
    val activityRule = IntentsTestRule(ContainerActivity::class.java)

    private val resources: Resources
        get() = InstrumentationRegistry.getInstrumentation().targetContext.resources

    protected fun openAddMealScreen() {
        onView(ViewMatchers.withId(R.id.floatingButton)).apply {
            check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
            perform(ViewActions.click())
        }
    }

    protected fun withDrawable(@DrawableRes id: Int) = object : TypeSafeMatcher<View>() {

        override fun describeTo(description: Description) {
            description.appendText("ImageView with drawable same as drawable with id $id")
        }

        override fun matchesSafely(view: View): Boolean {
            val imageView = view as? ImageView

            val actualDrawable = imageView?.drawable
            val expectedDrawable = view.context.getDrawable(id)

            imageView?.setImageDrawable(expectedDrawable)

            val actual = imageView?.drawable?.toBitmap()
            val expected = view.context.getDrawable(id)?.toBitmap()

            imageView?.setImageDrawable(actualDrawable)

            return actual?.sameAs(expected) ?: false
        }
    }

    protected fun getResourceUriFrom(resourceId: Int): Uri = with(resources) {
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(getResourcePackageName(resourceId))
            .appendPath(getResourceTypeName(resourceId))
            .appendPath(getResourceEntryName(resourceId))
            .build()
    }

    protected fun withTitle(title: String): Matcher<MealViewHolder> = object :
        BoundedMatcher<MealViewHolder, MealViewHolder>(MealViewHolder::class.java) {

        override fun matchesSafely(item: MealViewHolder): Boolean =
            item.binding.visual!!.title == title

        override fun describeTo(description: Description) {
            description.appendText("view holder with title: $title")
        }
    }

    protected fun clickOnView(@IdRes viewId: Int) = object : ViewAction {

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
