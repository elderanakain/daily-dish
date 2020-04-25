package io.krugosvet.dailydish.android.mainScreen

import android.net.*
import android.os.*
import io.krugosvet.dailydish.android.*
import io.krugosvet.dailydish.android.db.objects.meal.*
import io.krugosvet.dailydish.android.mainScreen.tab.*
import io.krugosvet.dailydish.android.network.*
import io.krugosvet.dailydish.android.network.json.*
import io.krugosvet.dailydish.android.utils.baseUi.*
import io.krugosvet.dailydish.android.utils.intent.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class StartupActivity :
    ImageProviderActivity(),
    DialogAddMeal.DialogAddMealListener {

  private lateinit var viewPagerAdapter: ViewPagerAdapter<BaseFragment>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    DailyDishApplication.appComponent.inject(this)

    setupViewPager()

    floatingButton.setOnClickListener {
      DialogAddMeal().show(fragmentManager, "")
    }

    getMeals()
  }

  override fun onAddButtonClick(mealTitle: String, mealDescription: String, parseDate: Date, mainImage: Uri) {
    val meal = Meal(mealTitle, mealDescription, parseDate, mainImage.toString())

    mealServicePipe
        .sendMeal(meal)
        .subscribe(
            object : BaseNetworkObserver<MealId>(this@StartupActivity) {
              override val onSuccessMessage = R.string.network_post_meal_success
              override val onErrorMessage: Int = R.string.network_post_meal_error

              override fun onSuccess(result: MealId) {
                super.onSuccess(result)
                meal.persist(realm, result.id)
              }
            }
        )
  }

  override fun onAccountStateChanged() {
    super.onAccountStateChanged()

    getMeals()
  }

  private fun setupViewPager() {
    viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
    viewPager.adapter = viewPagerAdapter

    viewPagerAdapter.addFragments(
        WhatToCookTodayFragment.newInstance(this), RecentlyCookedFragment.newInstance(this)
    )

    tabs.setupWithViewPager(viewPager, true)
  }

  private fun getMeals() =
      mealServicePipe
          .getMeals()
          .subscribe(
              object : BaseNetworkObserver<List<Meal>>(this@StartupActivity) {
                override val onSuccessMessage = R.string.network_get_meals_success
                override val onErrorMessage: Int = R.string.network_get_meals_error

                override fun onSuccess(result: List<Meal>) {
                  super.onSuccess(result)
                  realm.executeTransaction { it.insertOrUpdate(result) }
                }
              }
          )
}
