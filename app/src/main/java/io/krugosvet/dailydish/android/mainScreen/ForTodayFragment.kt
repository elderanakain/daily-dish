package io.krugosvet.dailydish.android.mainScreen

import android.os.Bundle
import io.krugosvet.dailydish.android.utils.getAscByDateMeals

class ForTodayFragment: MealListPageFragment() {

    override fun getAdapterItems() = getRealm().getAscByDateMeals()

    override fun getAdapterItemLimit() = 5

    companion object {
        fun newInstance(pageTitle: String) = ForTodayFragment().apply {
            arguments = Bundle().apply { putString(PAGE_TITLE, pageTitle) }
        }
    }
}
