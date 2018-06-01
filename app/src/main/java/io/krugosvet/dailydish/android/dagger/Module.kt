package io.krugosvet.dailydish.android.dagger

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
import android.support.annotation.NonNull
import android.support.v4.app.Fragment
import com.github.karczews.rxbroadcastreceiver.RxBroadcastReceivers
import com.ibm.bluemix.appid.android.api.AppID
import com.ibm.bluemix.appid.android.api.AppIDAuthorizationManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.FragmentKey
import dagger.multibindings.IntoMap
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManager
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManagerImpl
import io.krugosvet.dailydish.android.mainScreen.ForTodayFragment
import io.krugosvet.dailydish.android.mainScreen.MealListPageFragment
import io.krugosvet.dailydish.android.mainScreen.StartupActivity
import io.krugosvet.dailydish.android.network.BASE_URL
import io.krugosvet.dailydish.android.network.MealService
import io.krugosvet.dailydish.android.network.MealServicePipe
import io.krugosvet.dailydish.android.network.MealServicePipeImpl
import io.krugosvet.dailydish.android.utils.baseUi.BaseActivity
import io.krugosvet.dailydish.android.utils.baseUi.BaseFragment
import io.krugosvet.dailydish.android.utils.intent.ACCOUNT_STATE_CHANGE
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(subcomponents = [(BaseActivitySubcomponent::class)],
        includes = [BaseActivityModule.Declarations::class, AccountModule::class, NetworkModule::class])
class BaseActivityModule {

    @Module
    internal interface Declarations {
        @Binds
        @IntoMap
        @ActivityKey(BaseActivity::class)
        fun bindStartupActivityInjectorFactory(builder: BaseActivitySubcomponent.Builder):
                AndroidInjector.Factory<out Activity>

        @ContributesAndroidInjector
        fun contributeStartupActivity(): StartupActivity
    }

    @Singleton
    @NonNull
    @Provides
    fun providesAppId() = AppID.getInstance()

    @Singleton
    @NonNull
    @Provides
    fun providesRealm(): Realm = Realm.getDefaultInstance()
}

@Module(subcomponents = [BaseFragmentSubcomponent::class], includes = [BaseFragmentModule.Declarations::class, NetworkModule::class])
class BaseFragmentModule {

    @Module
    internal interface Declarations {
        @Binds
        @IntoMap
        @FragmentKey(BaseFragment::class)
        fun bindBaseFragmentInjectorFactory(builder: BaseFragmentSubcomponent.Builder):
                AndroidInjector.Factory<out Fragment>

        @ContributesAndroidInjector
        fun contributeForTodayFragment(): ForTodayFragment

        @ContributesAndroidInjector
        fun contributeMealListPageFragment(): MealListPageFragment
    }
}

@Module
class NetworkModule(private val appContext: Context) {

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
    fun provideMealServicePipe(): MealServicePipe = MealServicePipeImpl(appContext)
}

@Module
class AccountModule(private val appContext: Context) {

    @Singleton
    @NonNull
    @Provides
    fun providesAuthTokenManager(): AuthTokenManager =
            AuthTokenManagerImpl(appContext, AppIDAuthorizationManager(AppID.getInstance()))

    @Singleton
    @NonNull
    @Provides
    fun providesAccountStateChangeReceiver() = RxBroadcastReceivers.fromIntentFilter(appContext, IntentFilter(ACCOUNT_STATE_CHANGE))
}
