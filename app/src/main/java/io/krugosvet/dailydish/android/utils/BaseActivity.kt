package io.krugosvet.dailydish.android.utils

import android.view.Menu
import android.view.MenuItem
import com.ibm.bluemix.appid.android.api.AppID
import com.ibm.bluemix.appid.android.api.AuthorizationException
import com.ibm.bluemix.appid.android.api.AuthorizationListener
import com.ibm.bluemix.appid.android.api.tokens.AccessToken
import com.ibm.bluemix.appid.android.api.tokens.IdentityToken
import com.ibm.bluemix.appid.android.api.tokens.RefreshToken
import io.krugosvet.dailydish.android.R

abstract class BaseActivity : RealmActivity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            if (item.itemId == R.id.action_login) {
                val loginWidget = AppID.getInstance().loginWidget
                loginWidget.launch(this, object : AuthorizationListener {
                    override fun onAuthorizationCanceled() {
                    }

                    override fun onAuthorizationFailure(exception: AuthorizationException?) {
                    }

                    override fun onAuthorizationSuccess(accessToken: AccessToken?, identityToken: IdentityToken?, refreshToken: RefreshToken?) {
                    }
                })
                true
            } else super.onOptionsItemSelected(item)
}
