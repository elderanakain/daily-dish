package io.krugosvet.dailydish.android.mainScreen.tab

import android.content.Context
import android.os.Bundle
import android.view.View
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.utils.getAscByDateMeals

class WhatToCookTodayFragment : MealListPageFragment() {

    companion object {
        fun newInstance(context: Context) = WhatToCookTodayFragment().apply {
            arguments = Bundle().apply { putString(PAGE_TITLE, context.getString(R.string.what_to_cook_today)) }
        }
    }

    override fun getMealListQuery() =
            { realm.getAscByDateMeals(authTokenManager.userId()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.setMealsToShow(5)
    }

    override fun getEmptyLayoutText(): Int = R.string.layout_empty_what_to_cook_today
}
