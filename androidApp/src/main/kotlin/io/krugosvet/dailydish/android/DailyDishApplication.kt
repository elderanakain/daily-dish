package io.krugosvet.dailydish.android

import android.app.Application
import android.content.IntentFilter
import io.krugosvet.dailydish.android.architecture.injection.coreModule
import io.krugosvet.dailydish.android.reminder.ReminderService
import io.krugosvet.dailydish.android.reminder.injection.reminderModule
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotification
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationReceiver
import io.krugosvet.dailydish.android.service.injection.serviceModule
import io.krugosvet.dailydish.android.ui.injection.uiModule
import io.krugosvet.dailydish.common.core.commonModules
import io.krugosvet.dailydish.common.repository.db.appContext
import kotlinx.coroutines.CoroutineExceptionHandler
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

        appContext = this

        Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@DailyDishApplication)
            androidLogger(Level.ERROR)

            modules(
                commonModules.plus(
                    listOf(
                        serviceModule,
                        coreModule,
                        reminderModule,
                        uiModule,
                    )
                )
            )
        }

        get<ReminderService>().schedule()

        registerReceiver(
            get<ReminderNotificationReceiver>(),
            IntentFilter(ReminderNotification.CookedTodayAction.INTENT)
        )
    }
}

val errorHandler = CoroutineExceptionHandler { _, error -> Timber.e(error) }
