package io.krugosvet.dailydish.android.architecture.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.krugosvet.dailydish.android.DailyDishApplication
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.network.MealServicePipe
import io.krugosvet.dailydish.android.utils.baseUi.showLongSnackbar
import io.krugosvet.dailydish.android.utils.intent.ACCOUNT_STATE_CHANGE
import io.realm.Realm
import javax.inject.Inject

abstract class BaseActivity<VB : ViewDataBinding> :
  AppCompatActivity(),
  PopupMenu.OnMenuItemClickListener {

  @Inject
  lateinit var realm: Realm

  lateinit var binding: VB
    private set

  @Inject
  protected lateinit var mealServicePipe: MealServicePipe

  @get:LayoutRes
  protected abstract val layoutId: Int

  private lateinit var accountName: MenuItem

  protected abstract fun inject(appComponent: AppComponent)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.inflate(layoutInflater, layoutId, null, false)
    setContentView(binding.root)

    inject(DailyDishApplication.appComponent)
  }

  override fun onDestroy() {
    super.onDestroy()

    realm.close()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.action_bar, menu)
    this.accountName = menu.findItem(R.id.account_name)

    return true
  }

  override fun onOptionsItemSelected(item: MenuItem) =
    if (item.itemId == R.id.action_login) {
      showAuthMenuPopup(findViewById(item.itemId))
      true
    } else {
      super.onOptionsItemSelected(item)
    }

  override fun onMenuItemClick(item: MenuItem?): Boolean =
    when (item?.itemId) {
      R.id.authMenuSignOut -> signOut()
      else -> false
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

  protected open fun onAccountStateChanged() {
    sendBroadcast(Intent(ACCOUNT_STATE_CHANGE))
  }

  private fun signOut(): Boolean {
    onAccountStateChanged()

    return true
  }

  private fun showAuthMenuPopup(view: View) {
    val popup = PopupMenu(this, view)
    popup.setOnMenuItemClickListener(this)
    popup.show()
  }
}
