package io.krugosvet.dailydish.android.reminder.injection

import android.app.NotificationManager
import android.content.Context
import androidx.work.WorkManager
import io.krugosvet.dailydish.android.reminder.ReminderService
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationReceiver
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val reminderModule = module {
    single { WorkManager.getInstance(get()) }
    singleOf(::ReminderService)
    singleOf(::ReminderNotificationReceiver)

    single {
        ReminderNotificationService(
            get(),
            get(),
            get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager,
        )
    }
}
