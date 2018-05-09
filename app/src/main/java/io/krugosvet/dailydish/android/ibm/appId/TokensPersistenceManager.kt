package io.krugosvet.dailydish.android.ibm.appId

import android.content.Context
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager

const val APPID_ACCESS_TOKEN = "appid_access_token"
const val APPID_REFRESH_TOKEN = "appid_refresh_token"
const val APPID_TOKENS_PREF = "appid_tokens"
const val APPID_USER_NAME = "appid_user_name"
const val APPID_USER_ID = "appid_user_id"

class TokensPersistenceManager(private var context: Context, private var appIDAuthorizationManager: AppIDAuthorizationManager) {

    enum class StoredTokenState {
        EMPTY, ANONYMOUS, IDENTIFIED
    }

    private val sharedPreferences = context.getSharedPreferences(APPID_TOKENS_PREF, Context.MODE_PRIVATE)

    fun getStoredAccessToken() = sharedPreferences.getString(APPID_ACCESS_TOKEN, "")
    fun getStoredRefreshToken() = sharedPreferences.getString(APPID_REFRESH_TOKEN, "")
    fun isRefreshTokenExists() = !getStoredRefreshToken().isEmpty()
    fun getStoredUserName() = sharedPreferences.getString(APPID_USER_NAME, "")
    fun getStoredUserID() = sharedPreferences.getString(APPID_USER_ID, "")
    fun clearStoredTokens() = !sharedPreferences.edit().clear().commit()

    fun persistTokensOnDevice() {
        val identityToken = appIDAuthorizationManager.identityToken
        var refreshTokenString = ""
        val refreshToken = appIDAuthorizationManager.refreshToken
        if (refreshToken != null) {
            refreshTokenString = refreshToken.raw
        }

        sharedPreferences.edit()
                .putString(APPID_ACCESS_TOKEN, appIDAuthorizationManager.accessToken?.raw)
                .putString(APPID_USER_NAME, identityToken.name)
                .putString(APPID_USER_ID, identityToken.subject)
                .putString(APPID_REFRESH_TOKEN, refreshTokenString).apply()
    }
}
