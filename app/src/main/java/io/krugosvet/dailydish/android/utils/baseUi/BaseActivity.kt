package io.krugosvet.dailydish.android.utils.baseUi

import android.content.*
import android.net.*
import android.os.*
import android.view.*
import android.widget.*
import androidx.appcompat.app.*
import io.krugosvet.dailydish.android.*
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.network.*
import io.krugosvet.dailydish.android.utils.intent.*
import io.realm.*
import javax.inject.*

abstract class BaseActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

  @Inject
  lateinit var realm: Realm

  @Inject
  protected lateinit var mealServicePipe: MealServicePipe

  private lateinit var accountName: MenuItem

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    DailyDishApplication.appComponent.inject(this)
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
