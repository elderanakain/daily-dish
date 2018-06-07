package io.krugosvet.dailydish.android

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import io.krugosvet.dailydish.android.dagger.*
import io.realm.Realm

class DailyDishApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Fabric.with(this, Crashlytics())
        appComponent = buildComponent()
    }

    private fun buildComponent() = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .accountModule(AccountModule())
            .networkModule(NetworkModule()).build()
}
