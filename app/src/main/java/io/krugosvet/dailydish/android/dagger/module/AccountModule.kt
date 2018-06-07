package io.krugosvet.dailydish.android.dagger.module

import android.content.Context
import android.content.IntentFilter
import android.support.annotation.NonNull
import com.github.karczews.rxbroadcastreceiver.RxBroadcastReceivers
import com.ibm.bluemix.appid.android.api.AppID
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager
import dagger.Module
import dagger.Provides
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManager
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManagerImpl
import io.krugosvet.dailydish.android.utils.intent.ACCOUNT_STATE_CHANGE
import javax.inject.Singleton

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
