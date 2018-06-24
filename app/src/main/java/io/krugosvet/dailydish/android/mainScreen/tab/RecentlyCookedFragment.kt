package io.krugosvet.dailydish.android.mainScreen.tab

import android.content.Context
import android.os.Bundle
import android.view.View
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.utils.getDescByDateMeals

class RecentlyCookedFragment : MealListPageFragment() {

    companion object {
        fun newInstance(context: Context) = RecentlyCookedFragment().apply {
            arguments = Bundle().apply { putString(PAGE_TITLE, context.getString(R.string.recently_cooked)) }
        }
    }

    override fun getMealListQuery() =
            { realm.getDescByDateMeals(authTokenManager.userId()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setMealsToHide(5)
    }

    override fun getEmptyLayoutText(): Int = R.string.layout_empty_recently_cooked_text
}
