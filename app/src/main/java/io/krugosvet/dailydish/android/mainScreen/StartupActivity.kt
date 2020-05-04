package io.krugosvet.dailydish.android.mainScreen

import android.content.Context
import android.net.Uri
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import io.krugosvet.bindingcomponent.BindingComponent
import io.krugosvet.dailydish.android.BR
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.databinding.ActivityMainBinding
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.network.BaseNetworkObserver
import io.krugosvet.dailydish.android.network.json.MealId
import io.krugosvet.dailydish.android.utils.intent.ImageProviderActivity
import java.util.*

class StartupActivity :
  ImageProviderActivity<ActivityMainBinding, StartupActivity.Visual>(),
  DialogAddMeal.DialogAddMealListener {

  data class Visual(
    val onFloatingButtonClick: () -> Unit
  )

  override val bindingComponent = BindingComponent(R.layout.activity_main, this, BR.visual)

  override val parentContext: Context = this

  override fun onBind() {
    super.onBind()

    setupActionBarWithNavController(navController, bindingComponent.binding.drawerLayout)
    NavigationUI.setupWithNavController(bindingComponent.binding.navView, navController)

    getMeals()

    bindingComponent.visual.postValue(
      Visual { DialogAddMeal().show(supportFragmentManager, "") }
    )
  }

  override fun onSupportNavigateUp() =
    NavigationUI.navigateUp(navController, bindingComponent.binding.drawerLayout)

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
