package io.krugosvet.dailydish.android.mainScreen.tab

import android.os.Bundle
import android.view.View
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.utils.getAscByDateMeals
import io.realm.RealmQuery

class WhatToCookTodayFragment :
  MealListPageFragment() {

  override fun getMealListQuery(): () -> RealmQuery<Meal> = { realm.getAscByDateMeals() }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    adapter.setMealsToShow(5)
  }

  override fun getEmptyLayoutText(): Int = R.string.layout_empty_what_to_cook_today
}
