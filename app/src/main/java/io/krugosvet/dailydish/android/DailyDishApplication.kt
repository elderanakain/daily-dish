package io.krugosvet.dailydish.android

import android.app.*
import android.content.*
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
    Realm.init(this)
    appComponent = buildComponent()
    appContext = this
  }

  private fun buildComponent() = DaggerAppComponent.builder()
    .appModule(AppModule(this))
    .networkModule(NetworkModule()).build()
}
