package io.krugosvet.dailydish.android.mainScreen

import android.net.Uri
import android.os.Bundle
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.ui.BaseFragment
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.databinding.ActivityMainBinding
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.mainScreen.tab.RecentlyCookedFragment
import io.krugosvet.dailydish.android.mainScreen.tab.WhatToCookTodayFragment
import io.krugosvet.dailydish.android.network.BaseNetworkObserver
import io.krugosvet.dailydish.android.network.json.MealId
import io.krugosvet.dailydish.android.utils.intent.ImageProviderActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class StartupActivity :
  ImageProviderActivity<ActivityMainBinding>(),
  DialogAddMeal.DialogAddMealListener {

  override val layoutId: Int = R.layout.activity_main

  private lateinit var viewPagerAdapter: ViewPagerAdapter<BaseFragment<*>>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setupViewPager()

    floatingButton.setOnClickListener {
      DialogAddMeal().show(supportFragmentManager, "")
    }

    getMeals()
  }

  override fun inject(appComponent: AppComponent) {
    appComponent.inject(this)
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
