package io.krugosvet.dailydish.android

import android.app.Application
import android.content.Context
import io.krugosvet.dailydish.android.dagger.AppComponent
import io.krugosvet.dailydish.android.dagger.DaggerAppComponent
import io.krugosvet.dailydish.android.dagger.module.AppModule
import io.krugosvet.dailydish.android.dagger.module.NetworkModule
import io.realm.Realm

class DailyDishApplication :
    Application() {

  companion object {
    lateinit var appComponent: AppComponent
    lateinit var appContext: Context
  }

  override fun onCreate() {
    super.onCreate()

    Realm.init(this)

    appComponent = buildComponent()
    appContext = this
  }

  private fun buildComponent() =
      DaggerAppComponent
          .builder()
          .appModule(AppModule(this))
          .networkModule(NetworkModule())
          .build()
}
