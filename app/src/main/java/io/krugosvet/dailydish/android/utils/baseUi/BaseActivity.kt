package io.krugosvet.dailydish.android.utils.baseUi

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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


abstract class BaseActivity : AppCompatActivity() {

    @Inject
    protected lateinit var appID: AppID
    @Inject
    protected lateinit var authTokenManager: AuthTokenManager
    @Inject
    protected lateinit var realm: Realm
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
        if (authTokenManager.accountState == AccountState.ANONYMOUS) launchSingIn() else signOut()
        true
    } else super.onOptionsItemSelected(item)

    fun isInternetConnection(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnectedOrConnecting ?: false
    }

    fun getProgressBar() = findViewById<ProgressBar?>(R.id.progressBar)

    fun getParentCoordinatorLayout() =
            findViewById<View?>(R.id.parentCoordinatorLayout)

    private fun launchSingIn() = appID.loginWidget.launch(this, onAuthorizationSuccess())

    private fun signInExistingUser() = appID.signinWithRefreshToken(this,
            authTokenManager.refreshToken(), onAuthorizationSuccess())

    private fun signOut() {
        authTokenManager.clearTokens()
        onAccountStateChanged()
    }

    private fun onAuthorizationSuccess(): SimpleAuthorizationListener = object : SimpleAuthorizationListener() {
        override fun onAuthorizationSuccess(accessToken: AccessToken?, identityToken: IdentityToken?, refreshToken: RefreshToken?) {
            super.onAuthorizationSuccess(accessToken, identityToken, refreshToken)
            authTokenManager.persistTokens()
            onAccountStateChanged()
        }
    }

    private fun onAccountStateChanged() {
        runOnUiThread { accountName.title = authTokenManager.userName() }
        sendBroadcast(Intent(ACCOUNT_STATE_CHANGE))
    }
}
