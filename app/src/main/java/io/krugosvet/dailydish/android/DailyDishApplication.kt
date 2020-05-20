package io.krugosvet.dailydish.android

import android.app.Application
import io.krugosvet.dailydish.android.architecture.injection.dbModule
import io.krugosvet.dailydish.android.architecture.injection.module
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class DailyDishApplication :
  Application() {

  override fun onCreate() {
    super.onCreate()

    Timber.plant(Timber.DebugTree())

    startKoin {
      androidContext(this@DailyDishApplication)
      androidLogger(Level.INFO)

      modules(module, dbModule)
    }
  }
}
