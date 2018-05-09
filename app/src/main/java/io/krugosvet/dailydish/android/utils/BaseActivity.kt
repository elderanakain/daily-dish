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
import io.krugosvet.dailydish.android.ibm.appId.TokensPersistenceManager.StoredTokenState

abstract class BaseActivity : RealmActivity() {

    private val appID = AppID.getInstance()
    private lateinit var accountName: MenuItem
    private lateinit var tokensPersistenceManager: TokensPersistenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokensPersistenceManager = TokensPersistenceManager(this, AppIDAuthorizationManager(appID))

        if (tokensPersistenceManager.tokenState == StoredTokenState.IDENTIFIED) {
            signInExistingUser()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        this.accountName = menu.findItem(R.id.account_name)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = if (item.itemId == R.id.action_login) {
        if (tokensPersistenceManager.tokenState == StoredTokenState.ANONYMOUS) {
            launchSingIn()
        } else signOut()
        true
    } else super.onOptionsItemSelected(item)

    private fun launchSingIn() {
        appID.loginWidget.launch(this, object : SimpleAuthorizationListener() {
            override fun onAuthorizationSuccess(accessToken: AccessToken?, identityToken: IdentityToken?, refreshToken: RefreshToken?) {
                tokensPersistenceManager.persistTokensOnDevice()
                updateAccountName()
            }
        })
    }

    private fun signInExistingUser() {
        appID.signinWithRefreshToken(this, tokensPersistenceManager.getStoredRefreshToken(),
                object : SimpleAuthorizationListener() {
                    override fun onAuthorizationSuccess(accessToken: AccessToken?, identityToken: IdentityToken?, refreshToken: RefreshToken?) {
                        super.onAuthorizationSuccess(accessToken, identityToken, refreshToken)
                        tokensPersistenceManager.persistTokensOnDevice()
                        updateAccountName()
                    }
                })
    }

    private fun updateAccountName() {
        runOnUiThread { accountName.title = tokensPersistenceManager.getStoredUserName() }
    }

    private fun signOut() {
        tokensPersistenceManager.clearStoredTokens()
        updateAccountName()
    }
}
