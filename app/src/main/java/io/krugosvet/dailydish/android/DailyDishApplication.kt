package io.krugosvet.dailydish.android

import android.app.Application
import android.content.IntentFilter
import io.krugosvet.dailydish.android.reminder.ReminderService
import io.krugosvet.dailydish.android.reminder.injection.reminderModule
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotification
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationReceiver
import io.krugosvet.dailydish.android.repository.injection.repositoryModule
import io.krugosvet.dailydish.android.service.injection.serviceModule
import io.krugosvet.dailydish.android.ui.injection.uiModule
import io.krugosvet.dailydish.android.usecase.injection.useCaseModule
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
      androidLogger(Level.ERROR)

      modules(
        serviceModule,
        repositoryModule,
        coreModule,
        reminderModule,
        useCaseModule,
        uiModule
      )
    }

    get<ReminderService>().schedule()

    registerReceiver(
      get<ReminderNotificationReceiver>(),
      IntentFilter(ReminderNotification.CookedTodayAction.INTENT)
    )
  }
}
