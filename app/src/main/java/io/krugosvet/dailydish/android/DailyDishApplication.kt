package io.krugosvet.dailydish.android

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import com.crashlytics.android.Crashlytics
import com.ibm.bluemix.appid.android.api.AppID
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import io.fabric.sdk.android.Fabric
import io.krugosvet.dailydish.android.dagger.BaseActivityModule
import io.krugosvet.dailydish.android.dagger.DaggerDailyDishApplicationComponent
import io.realm.Realm
import javax.inject.Inject

class DailyDishApplication: Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()
        DaggerDailyDishApplicationComponent.builder()
                .baseActivityModule(BaseActivityModule(this))
                .build().inject(this)
        Realm.init(this)
        Fabric.with(this, Crashlytics())
        AppID.getInstance().initialize(this, resources.getString(R.string.authTenantId), AppID.REGION_UK)
    }

    override fun activityInjector(): AndroidInjector<Activity>? = dispatchingActivityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = dispatchingFragmentInjector
}
