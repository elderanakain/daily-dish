package io.krugosvet.dailydish.android.mainScreen.tab

import android.content.*
import android.os.*
import android.view.*
import io.krugosvet.dailydish.android.*
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.utils.*
import io.realm.RealmQuery

class WhatToCookTodayFragment : MealListPageFragment() {

  companion object {
    fun newInstance(context: Context) = WhatToCookTodayFragment().apply {
      arguments = Bundle().apply { putString(PAGE_TITLE, context.getString(R.string.what_to_cook_today)) }
    }
  }

  override fun getMealListQuery(): () -> RealmQuery<Meal> = { realm.getAscByDateMeals() }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    adapter.setMealsToShow(5)
  }

  override fun getEmptyLayoutText(): Int = R.string.layout_empty_what_to_cook_today
}
