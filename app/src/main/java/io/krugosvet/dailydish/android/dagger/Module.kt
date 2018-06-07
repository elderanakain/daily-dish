package io.krugosvet.dailydish.android.dagger

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
import io.krugosvet.dailydish.android.network.BASE_URL
import io.krugosvet.dailydish.android.network.MealService
import io.krugosvet.dailydish.android.network.MealServicePipe
import io.krugosvet.dailydish.android.network.MealServicePipeImpl
import io.krugosvet.dailydish.android.utils.intent.ACCOUNT_STATE_CHANGE
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val appContext: Context) {

    @Provides
    @NonNull
    @Singleton
    fun provideContext() = appContext

    @Provides
    @NonNull
    @Singleton
    fun provideRealm(): Realm = Realm.getDefaultInstance()
}

@Module
class NetworkModule {

    @Singleton
    @NonNull
    @Provides
    fun provideDailyDishService(): MealService = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .callFactory(OkHttpClient.Builder().build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build().create(MealService::class.java)

    @Singleton
    @NonNull
    @Provides
    fun provideMealServicePipe(context: Context): MealServicePipe = MealServicePipeImpl(context)
}

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
    fun providesAccountStateChangeReceiver(context: Context)
            = RxBroadcastReceivers.fromIntentFilter(context, IntentFilter(ACCOUNT_STATE_CHANGE))

    @Singleton
    @NonNull
    @Provides
    fun provideAppID(context: Context) = AppID.getInstance().initialize(context,
            context.resources.getString(R.string.authTenantId), AppID.REGION_UK)
}
