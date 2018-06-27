package io.krugosvet.dailydish.android.utils.baseUi

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.ibm.bluemix.appid.android.api.AppID
import com.ibm.bluemix.appid.android.api.tokens.AccessToken
import com.ibm.bluemix.appid.android.api.tokens.IdentityToken
import com.ibm.bluemix.appid.android.api.tokens.RefreshToken
import io.krugosvet.dailydish.android.DailyDishApplication
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.ibm.appId.AccountState
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManager
import io.krugosvet.dailydish.android.ibm.appId.SimpleAuthorizationListener
import io.krugosvet.dailydish.android.network.MealServicePipe
import io.krugosvet.dailydish.android.utils.intent.ACCOUNT_STATE_CHANGE
import io.realm.Realm
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    @Inject
    protected lateinit var appID: AppID
    @Inject
    protected lateinit var authTokenManager: AuthTokenManager
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
        if (authTokenManager.accountState == AccountState.IDENTIFIED) signInExistingUser()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = if (item.itemId == R.id.action_login) {
        showAuthMenuPopup(findViewById(item.itemId))
        true
    } else super.onOptionsItemSelected(item)

    override fun onMenuItemClick(item: MenuItem?): Boolean = when (item?.itemId) {
        R.id.authMenuSignIn -> authorizeUser(true)
        R.id.authMenuSignUp -> authorizeUser(false)
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
        runOnUiThread { accountName.title = authTokenManager.userName() }
        sendBroadcast(Intent(ACCOUNT_STATE_CHANGE))
    }

    private fun authorizeUser(toSignIn: Boolean): Boolean = when {
        isInternetConnection() -> {
            if (toSignIn) appID.loginWidget.launch(this, onAuthorizationSuccess())
            else appID.loginWidget.launchSignUp(this, onAuthorizationSuccess()); true
        }
        else -> {
            noInternetConnectionError(); false
        }
    }

    private fun signInExistingUser() =
            appID.signinWithRefreshToken(this, authTokenManager.refreshToken(), onAuthorizationSuccess())

    private fun signOut(): Boolean {
        authTokenManager.clearTokens()
        onAccountStateChanged()
        return true
    }

    private fun onAuthorizationSuccess(): SimpleAuthorizationListener = object : SimpleAuthorizationListener() {
        override fun onAuthorizationSuccess(accessToken: AccessToken?, identityToken: IdentityToken?, refreshToken: RefreshToken?) {
            super.onAuthorizationSuccess(accessToken, identityToken, refreshToken)
            authTokenManager.persistTokens()
            onAccountStateChanged()
        }
    }

    private fun showAuthMenuPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(if (authTokenManager.isUserIdentified()) R.menu.auth_action_bar else R.menu.not_auth_action_bar, popup.menu)
        popup.setOnMenuItemClickListener(this)
        popup.show()
    }
}
