package io.krugosvet.dailydish.android.architecture.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.findNavController
import io.krugosvet.bindingcomponent.IBindingContainer
import io.krugosvet.dailydish.android.DailyDishApplication
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.network.MealServicePipe
import io.krugosvet.dailydish.android.utils.baseUi.showLongSnackbar
import io.realm.Realm
import javax.inject.Inject

abstract class BaseActivity<TBinding: ViewDataBinding, TVisual> :
  AppCompatActivity(),
  IBindingContainer<TBinding, TVisual> {

  @Inject
  protected lateinit var realm: Realm

  @Inject
  protected lateinit var mealServicePipe: MealServicePipe

  protected val navController: NavController by lazy {
    findNavController(R.id.hostFragment)
  }

  protected abstract fun inject(appComponent: AppComponent)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    inject(DailyDishApplication.appComponent)
  }

  override fun onSupportNavigateUp() = navController.navigateUp()

  override fun onDestroy() {
    super.onDestroy()

    realm.close()
  }

  fun isInternetConnection(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    return cm.activeNetworkInfo?.isConnectedOrConnecting ?: false
  }

  fun getProgressBar() = findViewById<ProgressBar?>(R.id.progressBar)

  fun getParentCoordinatorLayout() = findViewById<View?>(R.id.parentCoordinatorLayout)

  protected fun noInternetConnectionError() {
    showLongSnackbar(this, R.string.network_no_internet_connection)
  }
}
