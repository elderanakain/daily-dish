package io.krugosvet.dailydish.android.mainScreen.tab

import android.content.Context
import android.os.Bundle
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.utils.getAscByDateMeals

class EarlierCookedFragment : MealListPageFragment() {

    companion object {
        fun newInstance(context: Context) = EarlierCookedFragment().apply {
            arguments = Bundle().apply { putString(PAGE_TITLE, context.getString(R.string.earlier)) }
        }
    }

    override fun getMealListQuery() = { realm.getAscByDateMeals(authTokenManager.userId()) }
}
