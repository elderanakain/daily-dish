package io.krugosvet.dailydish.android.mainScreen.tab

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.architecture.ui.BaseActivity
import io.krugosvet.dailydish.android.architecture.ui.BaseFragment
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.databinding.FragmentMealListBinding
import io.krugosvet.dailydish.android.db.objects.meal.Meal
import io.krugosvet.dailydish.android.mainScreen.MealListAdapter
import io.krugosvet.dailydish.android.mainScreen.MealListAdapterPipe
import io.krugosvet.dailydish.android.network.BaseNetworkObserver
import io.krugosvet.dailydish.android.network.json.UpdateDateMeal
import io.krugosvet.dailydish.android.network.json.UpdateImageMeal
import io.krugosvet.dailydish.android.utils.ViewPagerFragment
import io.krugosvet.dailydish.android.utils.baseUi.showLongSnackbar
import io.krugosvet.dailydish.android.utils.getCurrentDate
import io.krugosvet.dailydish.android.utils.intent.ImageProviderActivity
import io.realm.RealmQuery

const val PAGE_TITLE = "pageTitle"

abstract class MealListPageFragment :
  BaseFragment<FragmentMealListBinding>(),
  ViewPagerFragment,
  MealListAdapterPipe {

  protected lateinit var adapter: MealListAdapter

  override val layoutId: Int = R.layout.fragment_meal_list

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    adapter = MealListAdapter(activity as ImageProviderActivity<*>, getMealListQuery(), this)

    binding.apply {
      mealList.adapter = adapter
      emptyLayout.emptyLayoutText.text = getString(getEmptyLayoutText())
    }
  }

  override fun getFragmentTitle() = arguments?.getString(PAGE_TITLE) ?: ""

  override fun inject(appComponent: AppComponent) {
    appComponent.inject(this)
  }

  override fun deleteMeal(meal: Meal) =
    mealServicePipe
      .deleteMeal(meal)
      .subscribe(
        object : BaseNetworkObserver<Void>(activity as BaseActivity<*>) {

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
      binding.apply {
        mealList.visibility = if (isEmpty) View.GONE else View.VISIBLE
        emptyLayout.root.visibility = if (isEmpty) View.VISIBLE else View.GONE
      }
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
        object : BaseNetworkObserver<Void>(activity as BaseActivity<*>) {

          override val onErrorMessage = R.string.network_put_meal_error
          override val onSuccessMessage = R.string.network_put_meal_success

          override fun onComplete() {
            super.onComplete()
            meal.removeMainImage(realm)
          }
        }
      )

  override fun showLongSnackbar(message: Int) =
    showLongSnackbar(activity as BaseActivity<*>, message)

  override fun changeMealCookedDate(meal: Meal) =
    mealServicePipe
      .updateDateMeal(UpdateDateMeal(meal.id, getCurrentDate()))
      .subscribe(
        object : BaseNetworkObserver<Void>(activity as BaseActivity<*>) {

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
