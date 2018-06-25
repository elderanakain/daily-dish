package io.krugosvet.dailydish.android.mainScreen

import android.net.Uri
import android.os.Bundle
import io.krugosvet.dailydish.android.DailyDishApplication
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.mainScreen.tab.RecentlyCookedFragment
import io.krugosvet.dailydish.android.mainScreen.tab.WhatToCookTodayFragment
import io.krugosvet.dailydish.android.network.BaseNetworkObserver
import io.krugosvet.dailydish.android.network.json.MealId
import io.krugosvet.dailydish.android.utils.baseUi.BaseFragment
import io.krugosvet.dailydish.android.utils.baseUi.showLongSnackbar
import io.krugosvet.dailydish.android.utils.intent.ImageProviderActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class StartupActivity : ImageProviderActivity(), DialogAddMeal.DialogAddMealListener {

    private lateinit var viewPagerAdapter: ViewPagerAdapter<BaseFragment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DailyDishApplication.appComponent.inject(this)

        setupViewPager()

        floatingButton.setOnClickListener {
            when {
                !authTokenManager.isUserIdentified() -> showLongSnackbar(this, R.string.not_auth_add_meal_error)
                !isInternetConnection() -> noInternetConnectionError()
                else -> DialogAddMeal().show(fragmentManager, "")
            }
        }

        mealServicePipe.getMeals().subscribe(object : BaseNetworkObserver<List<Meal>>(this@StartupActivity) {
            override val onSuccessMessage = R.string.network_get_meals_success
            override val onErrorMessage: Int = R.string.network_get_meals_error

            override fun onSuccess(result: List<Meal>) {
                super.onSuccess(result)
                realm.executeTransaction { it.insertOrUpdate(result) }
            }
        })
    }

    override fun onAddButtonClick(mealTitle: String, mealDescription: String, parseDate: Date, mainImage: Uri) {
        val meal = Meal(mealTitle, mealDescription, parseDate,
                mainImage.toString(), authTokenManager.userId())
        mealServicePipe.sendMeal(meal).subscribe(object : BaseNetworkObserver<MealId>(this@StartupActivity) {
            override val onSuccessMessage = R.string.network_post_meal_success
            override val onErrorMessage: Int = R.string.network_post_meal_error

            override fun onSuccess(result: MealId) {
                super.onSuccess(result)
                meal.persist(realm, result.id)
            }
        })
    }

    private fun setupViewPager() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = viewPagerAdapter
        viewPagerAdapter.addFragments(WhatToCookTodayFragment.newInstance(this),
                RecentlyCookedFragment.newInstance(this))
        tabs.setupWithViewPager(viewPager, true)
    }
}
