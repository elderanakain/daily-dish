package io.krugosvet.dailydish.android.mainScreen.tab

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import io.krugosvet.bindingcomponent.BindingComponent
import io.krugosvet.dailydish.android.BR
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.ui.BaseActivity
import io.krugosvet.dailydish.android.architecture.ui.BaseFragment
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.databinding.ListMealBinding
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.mainScreen.MealListAdapter
import io.krugosvet.dailydish.android.mainScreen.MealListAdapterPipe
import io.krugosvet.dailydish.android.network.BaseNetworkObserver
import io.krugosvet.dailydish.android.network.json.UpdateDateMeal
import io.krugosvet.dailydish.android.network.json.UpdateImageMeal
import io.krugosvet.dailydish.android.utils.baseUi.showLongSnackbar
import io.krugosvet.dailydish.android.utils.getCurrentDate
import io.krugosvet.dailydish.android.utils.intent.ImageProviderActivity
import io.realm.RealmQuery

abstract class MealListPageFragment :
  BaseFragment<ListMealBinding, MealListPageFragment.Visual>(),
  MealListAdapterPipe {

  data class Visual(
    val emptyLayoutText: String,
    val adapter: MealListAdapter,
    val isMealListVisible: Boolean = false,
    val isEmptyLayoutVisible: Boolean = true
  )

  override val parentContext: Context
    get() = requireContext()

  override val bindingComponent = BindingComponent(R.layout.fragment_meal_list, this, BR.visual)

  protected lateinit var adapter: MealListAdapter

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    adapter = MealListAdapter(activity as ImageProviderActivity<*, *>, getMealListQuery(), this)

    bindingComponent.visual.postValue(
      Visual(
        getString(getEmptyLayoutText()),
        adapter
      )
    )
  }

  override fun inject(appComponent: AppComponent) = appComponent.inject(this)

  override fun deleteMeal(meal: Meal) =
    mealServicePipe
      .deleteMeal(meal)
      .subscribe(
        object : BaseNetworkObserver<Void>(activity as BaseActivity<*, *>) {

          override val onErrorMessage = R.string.network_delete_meal_error
          override val onSuccessMessage = R.string.network_delete_meal_success

          override fun onComplete() {
            super.onComplete()
            meal.delete(realm)
          }
        }
      )

  override fun onMealListChange(isEmpty: Boolean) {
    if (isAdded) {
      bindingComponent.visual.postValue(
        bindingComponent.visual.value?.copy(
          isMealListVisible = !isEmpty,
          isEmptyLayoutVisible = isEmpty
        )
      )
    }
  }

  override fun changeMealMainImage(meal: Meal, image: Uri) =
    mealServicePipe
      .updateImageMeal(UpdateImageMeal(meal.id, image.toString()))
      .subscribe(
        object : BaseNetworkObserver<Void>((activity as? Activity)) {

          override val onErrorMessage = R.string.network_put_meal_error
          override val onSuccessMessage = R.string.network_put_meal_success

          override fun onComplete() {
            super.onComplete()
            meal.changeMainImage(realm, image)
          }
        }
      )

  override fun removeMealMainImage(meal: Meal) =
    mealServicePipe
      .updateImageMeal(UpdateImageMeal(meal.id, ""))
      .subscribe(
        object : BaseNetworkObserver<Void>(activity as BaseActivity<*, *>) {

          override val onErrorMessage = R.string.network_put_meal_error
          override val onSuccessMessage = R.string.network_put_meal_success

          override fun onComplete() {
            super.onComplete()
            meal.removeMainImage(realm)
          }
        }
      )

  override fun showLongSnackbar(message: Int) =
    showLongSnackbar(activity as BaseActivity<*, *>, message)

  override fun changeMealCookedDate(meal: Meal) =
    mealServicePipe
      .updateDateMeal(UpdateDateMeal(meal.id, getCurrentDate()))
      .subscribe(
        object : BaseNetworkObserver<Void>(activity as BaseActivity<*, *>) {

          override val onErrorMessage = R.string.network_put_meal_error
          override val onSuccessMessage = R.string.network_put_meal_success

          override fun onComplete() {
            super.onComplete()
            meal.updateDateToCurrent(realm)
          }
        }
      )

  protected abstract fun getMealListQuery(): () -> RealmQuery<Meal>

  @StringRes
  protected abstract fun getEmptyLayoutText(): Int
}
