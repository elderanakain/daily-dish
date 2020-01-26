package io.krugosvet.dailydish.android.ibm.appId

import com.ibm.bluemix.appid.android.api.*
import com.ibm.bluemix.appid.android.api.tokens.*

open class SimpleAuthorizationListener: AuthorizationListener {
  override fun onAuthorizationCanceled() {}
  override fun onAuthorizationFailure(exception: AuthorizationException?) {}
  override fun onAuthorizationSuccess(accessToken: AccessToken?, identityToken: IdentityToken?, refreshToken: RefreshToken?) {}
}
