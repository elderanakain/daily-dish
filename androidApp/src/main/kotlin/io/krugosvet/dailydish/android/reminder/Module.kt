package io.krugosvet.dailydish.android.reminder

import android.app.NotificationManager
import android.content.Context
import androidx.work.WorkManager
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationReceiver
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val reminderModule = module {
    single { WorkManager.getInstance(get()) }
    singleOf(::ReminderService)
    singleOf(::ReminderNotificationReceiver)
    singleOf(::ReminderNotificationService)

    single { get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
}
