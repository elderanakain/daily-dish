package io.krugosvet.dailydish.android.mainScreen

import android.content.Context
import android.net.Uri
import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import io.krugosvet.bindingcomponent.BindingComponent
import io.krugosvet.dailydish.android.BR
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.ui.BaseFragment
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.mainScreen.tab.RecentlyCookedFragment
import io.krugosvet.dailydish.android.mainScreen.tab.WhatToCookTodayFragment
import io.krugosvet.dailydish.android.network.BaseNetworkObserver
import io.krugosvet.dailydish.android.network.json.MealId
import io.krugosvet.dailydish.android.utils.intent.ImageProviderActivity
import java.util.*

@BindingAdapter("adapter")
fun TabLayout.setAdapter(viewPager: ViewPager?) {
  setupWithViewPager(viewPager, true)
}

class StartupActivity :
  ImageProviderActivity<StartupActivity.Visual>(),
  DialogAddMeal.DialogAddMealListener {

  data class Visual(
    val adapter: ViewPagerAdapter<*>,
    val onFloatingButtonClick: () -> Unit
  )

  override val bindingComponent = BindingComponent(R.layout.activity_main, this, BR.visual)

  override val parentContext: Context = this

  override fun onBind() {
    getMeals()

    viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
    visual.postValue(
      Visual(viewPagerAdapter) { DialogAddMeal().show(supportFragmentManager, "") }
    )

    viewPagerAdapter.addFragments(
      WhatToCookTodayFragment.newInstance(this), RecentlyCookedFragment.newInstance(this)
    )
  }

  private lateinit var viewPagerAdapter: ViewPagerAdapter<BaseFragment<*>>

  override fun inject(appComponent: AppComponent) = appComponent.inject(this)

  override fun onAddButtonClick(
    mealTitle: String,
    mealDescription: String,
    parseDate: Date,
    mainImage: Uri
  ) {
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
