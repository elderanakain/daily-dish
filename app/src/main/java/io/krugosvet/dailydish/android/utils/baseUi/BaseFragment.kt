package io.krugosvet.dailydish.android.utils.baseUi

import android.content.*
import android.support.v4.app.*
import com.ibm.bluemix.appid.android.api.*
import io.krugosvet.dailydish.android.ibm.appId.*
import io.krugosvet.dailydish.android.network.*
import io.realm.*
import javax.inject.*

abstract class BaseFragment : Fragment() {

  @Inject
  protected lateinit var realm: Realm
  @Inject
  protected lateinit var appID: AppID
  @Inject
  protected lateinit var authTokenManager: AuthTokenManager
  @Inject
  protected lateinit var mealServicePipe: MealServicePipe

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
