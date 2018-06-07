package io.krugosvet.dailydish.android.utils.baseUi

import android.content.Context
import android.support.v4.app.Fragment
import com.ibm.bluemix.appid.android.api.AppID
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManager
import io.realm.Realm
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    @Inject
    protected lateinit var realm: Realm
    @Inject
    protected lateinit var appID: AppID
    @Inject
    protected lateinit var authTokenManager: AuthTokenManager

    protected abstract fun initInjection()

    override fun onAttach(context: Context?) {
        initInjection()
        super.onAttach(context)
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
