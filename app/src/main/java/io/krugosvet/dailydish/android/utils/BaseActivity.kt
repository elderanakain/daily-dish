package io.krugosvet.dailydish.android.utils

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.ibm.bluemix.appid.android.api.AppID
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager
import com.ibm.bluemix.appid.android.api.tokens.AccessToken
import com.ibm.bluemix.appid.android.api.tokens.IdentityToken
import com.ibm.bluemix.appid.android.api.tokens.RefreshToken
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.ibm.appId.SimpleAuthorizationListener
import io.krugosvet.dailydish.android.ibm.appId.TokensPersistenceManager

abstract class BaseActivity : RealmActivity() {

    private val appID = AppID.getInstance()
    private lateinit var tokensPersistenceManager: TokensPersistenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokensPersistenceManager = TokensPersistenceManager(this, AppIDAuthorizationManager(appID))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = if (item.itemId == R.id.action_login) {
        if (tokensPersistenceManager.isRefreshTokenExists()) signInExistingUser() else launchSingIn()
        true
    } else super.onOptionsItemSelected(item)

    private fun launchSingIn() {
        appID.loginWidget.launch(this, object : SimpleAuthorizationListener() {
            override fun onAuthorizationSuccess(accessToken: AccessToken?, identityToken: IdentityToken?, refreshToken: RefreshToken?) {
                tokensPersistenceManager.persistTokensOnDevice()
            }
        })
    }

    private fun signInExistingUser() {
        appID.signinWithRefreshToken(this, tokensPersistenceManager.getStoredRefreshToken(),
                object : SimpleAuthorizationListener() {
                    override fun onAuthorizationSuccess(accessToken: AccessToken?, identityToken: IdentityToken?, refreshToken: RefreshToken?) {
                        super.onAuthorizationSuccess(accessToken, identityToken, refreshToken)

                    }
                })
    }
}
