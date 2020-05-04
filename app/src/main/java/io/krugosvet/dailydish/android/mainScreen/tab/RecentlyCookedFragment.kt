package io.krugosvet.dailydish.android.mainScreen.tab

import android.os.Bundle
import android.view.View
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.utils.getDescByDateMeals
import io.realm.RealmQuery

class RecentlyCookedFragment :
  MealListPageFragment() {

  override fun getMealListQuery(): () -> RealmQuery<Meal> = { realm.getDescByDateMeals() }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    adapter.setMealsToHide(5)
  }

  override fun getEmptyLayoutText(): Int = R.string.layout_empty_recently_cooked_text
}
