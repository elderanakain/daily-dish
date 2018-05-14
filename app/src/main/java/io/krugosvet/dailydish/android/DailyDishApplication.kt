package io.krugosvet.dailydish.android

import android.app.Activity
import android.app.Application
import com.ibm.bluemix.appid.android.api.AppID
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import io.krugosvet.dailydish.android.dagger.DaggerDailyDishApplicationComponent
import io.realm.Realm
import javax.inject.Inject

class DailyDishApplication: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        DaggerDailyDishApplicationComponent.create().inject(this)
        Realm.init(this)
        AppID.getInstance().initialize(this, resources.getString(R.string.authTenantId), AppID.REGION_UK)
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return dispatchingActivityInjector
    }
}
