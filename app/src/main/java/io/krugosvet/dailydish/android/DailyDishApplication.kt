package io.krugosvet.dailydish.android

import android.app.Application
import android.content.Context
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.fabric.sdk.android.Fabric
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.dagger.DaggerAppComponent
import io.krugosvet.dailydish.android.dagger.module.AccountModule
import io.krugosvet.dailydish.android.dagger.module.AppModule
import io.krugosvet.dailydish.android.dagger.module.NetworkModule
import io.realm.Realm

class DailyDishApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        Fabric.with(this, Crashlytics())
        Realm.init(this)
        appComponent = buildComponent()
        appContext = this

        if (BuildConfig.DEBUG) {
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                    .build())
        }
    }

    private fun buildComponent() = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .accountModule(AccountModule())
            .networkModule(NetworkModule()).build()
}
