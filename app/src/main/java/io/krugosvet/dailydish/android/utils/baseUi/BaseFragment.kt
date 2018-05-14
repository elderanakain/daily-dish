package io.krugosvet.dailydish.android.utils.baseUi

import android.os.Bundle
import android.view.MenuItem
import com.ibm.bluemix.appid.android.api.AppID
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager
import io.krugosvet.dailydish.android.ibm.appId.TokensPersistenceManager
import io.krugosvet.dailydish.android.utils.RealmFragment

abstract class BaseFragment: RealmFragment() {

    private val appID = AppID.getInstance()
    private lateinit var accountName: MenuItem
    protected lateinit var tokensPersistenceManager: TokensPersistenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tokensPersistenceManager = TokensPersistenceManager(context!!, AppIDAuthorizationManager(appID))
    }
}
