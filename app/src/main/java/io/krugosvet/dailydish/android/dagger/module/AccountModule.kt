package io.krugosvet.dailydish.android.dagger.module

import android.content.*
import android.support.annotation.*
import com.github.karczews.rxbroadcastreceiver.*
import com.ibm.bluemix.appid.android.api.*
import dagger.*
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.ibm.appId.*
import io.krugosvet.dailydish.android.utils.intent.*
import javax.inject.*

@Module
class AccountModule {

  @Singleton
  @NonNull
  @Provides
  fun providesAuthTokenManager(context: Context): AuthTokenManager =
    AuthTokenManagerImpl(context, AppIDAuthorizationManager(AppID.getInstance()
      .initialize(context, context.resources.getString(R.string.authTenantId),
        AppID.REGION_UK)))

  @Singleton
  @NonNull
  @Provides
  fun providesAccountStateChangeReceiver(context: Context) =
    RxBroadcastReceivers.fromIntentFilter(context, IntentFilter(ACCOUNT_STATE_CHANGE))

  @Singleton
  @NonNull
  @Provides
  fun provideAppID(context: Context) = AppID.getInstance().initialize(context,
    context.resources.getString(R.string.authTenantId), AppID.REGION_UK)
}
