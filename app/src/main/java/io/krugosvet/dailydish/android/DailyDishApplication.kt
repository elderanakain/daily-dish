package io.krugosvet.dailydish.android

import android.app.*
import android.content.*
import com.crashlytics.android.*
import com.facebook.stetho.*
import com.uphyca.stetho_realm.*
import io.fabric.sdk.android.*
import io.krugosvet.dailydish.android.dagger.*
import io.krugosvet.dailydish.android.dagger.module.*
import io.realm.*

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
