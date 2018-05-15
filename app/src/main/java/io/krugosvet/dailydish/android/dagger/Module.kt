package io.krugosvet.dailydish.android.dagger

import android.app.Activity
import android.content.Context
import android.content.IntentFilter
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
import io.krugosvet.dailydish.android.mainScreen.ForTodayFragment
import io.krugosvet.dailydish.android.mainScreen.MealListPageFragment
import io.krugosvet.dailydish.android.mainScreen.StartupActivity
import io.krugosvet.dailydish.android.utils.baseUi.BaseActivity
import io.krugosvet.dailydish.android.utils.baseUi.BaseFragment
import io.krugosvet.dailydish.android.utils.intent.ACCOUNT_STATE_CHANGE
import io.realm.Realm

@Module(subcomponents = [(BaseActivitySubcomponent::class)], includes = [BaseActivityModule.Declarations::class])
internal class BaseActivityModule(private val context: Context) {

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

    @Provides
    fun providesTokenPersistenceManager() = AuthTokenManager(context, AppIDAuthorizationManager(AppID.getInstance()))

    @Provides
    fun providesAppId() = AppID.getInstance()

    @Provides
    fun providesRealm(): Realm = Realm.getDefaultInstance()

    @Provides
    fun providesAccountStateChangeReceiver() = RxBroadcastReceivers.fromIntentFilter(context, IntentFilter(ACCOUNT_STATE_CHANGE))
}

@Module(subcomponents = [BaseFragmentSubcomponent::class], includes = [BaseFragmentModule.Declarations::class])
internal class BaseFragmentModule {

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
