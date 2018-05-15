package io.krugosvet.dailydish.android.ibm.appId

import android.annotation.SuppressLint
import android.content.Context
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager

const val APPID_ACCESS_TOKEN = "appid_access_token"
const val APPID_REFRESH_TOKEN = "appid_refresh_token"
const val APPID_TOKENS_PREF = "appid_tokens"
const val APPID_USER_NAME = "appid_user_name"
const val APPID_USER_ID = "appid_user_id"

class AuthTokenManager constructor(context: Context, private var appIDAuthorizationManager: AppIDAuthorizationManager) {

    enum class StoredTokenState {
        ANONYMOUS, IDENTIFIED
    }

    private val sharedPreferences = context.getSharedPreferences(APPID_TOKENS_PREF, Context.MODE_PRIVATE)

    var tokenState: StoredTokenState = if (isRefreshTokenExists()) StoredTokenState.IDENTIFIED else StoredTokenState.ANONYMOUS

    fun getStoredAccessToken() = sharedPreferences.getString(APPID_ACCESS_TOKEN, "")
    fun getStoredRefreshToken(): String = sharedPreferences.getString(APPID_REFRESH_TOKEN, "")
    private fun isRefreshTokenExists() = !getStoredRefreshToken().isEmpty()
    fun getStoredUserName(): String = sharedPreferences.getString(APPID_USER_NAME, "")
    fun userId(): String = sharedPreferences.getString(APPID_USER_ID, "")

    fun clearStoredTokens() {
        !sharedPreferences.edit().clear().commit()
        tokenState = StoredTokenState.ANONYMOUS
    }

    @SuppressLint("ApplySharedPref")
    fun persistTokensOnDevice() {
        val identityToken = appIDAuthorizationManager.identityToken

        sharedPreferences.edit()
                .putString(APPID_ACCESS_TOKEN, appIDAuthorizationManager.accessToken?.raw)
                .putString(APPID_USER_NAME, identityToken.name)
                .putString(APPID_USER_ID, identityToken.subject)
                .putString(APPID_REFRESH_TOKEN, appIDAuthorizationManager.refreshToken.raw).commit()
        tokenState = StoredTokenState.IDENTIFIED
    }
}
