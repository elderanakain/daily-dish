package io.krugosvet.dailydish.android.ibm.appId

import com.ibm.bluemix.appid.android.api.AuthorizationException
import com.ibm.bluemix.appid.android.api.AuthorizationListener
import com.ibm.bluemix.appid.android.api.tokens.AccessToken
import com.ibm.bluemix.appid.android.api.tokens.IdentityToken
import com.ibm.bluemix.appid.android.api.tokens.RefreshToken

open class SimpleAuthorizationListener: AuthorizationListener {
    override fun onAuthorizationCanceled() {}
    override fun onAuthorizationFailure(exception: AuthorizationException?) {}
    override fun onAuthorizationSuccess(accessToken: AccessToken?, identityToken: IdentityToken?, refreshToken: RefreshToken?) {}
}
