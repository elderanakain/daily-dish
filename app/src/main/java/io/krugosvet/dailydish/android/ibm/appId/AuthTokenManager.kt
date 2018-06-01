package io.krugosvet.dailydish.android.ibm.appId

import android.annotation.SuppressLint
import android.content.Context
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager

const val APPID_ACCESS_TOKEN = "appid_access_token"
const val APPID_REFRESH_TOKEN = "appid_refresh_token"
const val APPID_TOKENS_PREF = "appid_tokens"
const val APPID_USER_NAME = "appid_user_name"
const val APPID_USER_ID = "appid_user_id"

enum class AccountState {
    ANONYMOUS, IDENTIFIED
}

interface AuthTokenManager {
    var accountState: AccountState

    fun persistTokens()
    fun refreshToken(): String
    fun userName(): String
    fun userId(): String
    fun clearTokens()
}

internal class AuthTokenManagerImpl constructor(context: Context, private var appIDAuthorizationManager: AppIDAuthorizationManager)
    : AuthTokenManager {

    private val sharedPreferences = context.getSharedPreferences(APPID_TOKENS_PREF, Context.MODE_PRIVATE)

    override var accountState: AccountState = if (isRefreshTokenExists()) AccountState.IDENTIFIED else AccountState.ANONYMOUS

    override fun refreshToken(): String = sharedPreferences.getString(APPID_REFRESH_TOKEN, "")

    override fun userName(): String = sharedPreferences.getString(APPID_USER_NAME, "")

    override fun userId(): String = sharedPreferences.getString(APPID_USER_ID, "")

    override fun clearTokens() {
        !sharedPreferences.edit().clear().commit()
        accountState = AccountState.ANONYMOUS
    }

    @SuppressLint("ApplySharedPref")
    override fun persistTokens() {
        val identityToken = appIDAuthorizationManager.identityToken

        sharedPreferences.edit()
                .putString(APPID_ACCESS_TOKEN, appIDAuthorizationManager.accessToken?.raw)
                .putString(APPID_USER_NAME, identityToken.name)
                .putString(APPID_USER_ID, identityToken.subject)
                .putString(APPID_REFRESH_TOKEN, appIDAuthorizationManager.refreshToken.raw).commit()
        accountState = AccountState.IDENTIFIED
    }

    private fun isRefreshTokenExists() = !refreshToken().isEmpty()
}
