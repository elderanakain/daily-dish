package io.krugosvet.dailydish.android.mainScreen

import android.os.Bundle

class ForTodayFragment: MealListPageFragment() {

    companion object {
        fun newInstance(pageTitle: String) = ForTodayFragment().apply {
            arguments = Bundle().apply { putString(PAGE_TITLE, pageTitle) }
        }
    }
}
