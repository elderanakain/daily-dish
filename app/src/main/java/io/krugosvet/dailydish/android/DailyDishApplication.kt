package io.krugosvet.dailydish.android

import android.app.Application
import io.krugosvet.android.reminder.ReminderService
import io.krugosvet.android.reminder.injection.reminderModule
import io.krugosvet.dailydish.android.architecture.injection.module
import io.krugosvet.dailydish.android.repository.injection.repositoryModule
import io.krugosvet.dailydish.core.injection.coreModule
import org.koin.android.ext.android.get
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

      modules(
        module,
        repositoryModule,
        coreModule,
        reminderModule
      )
    }

    get<ReminderService>().schedule()
  }
}
