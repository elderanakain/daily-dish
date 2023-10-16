package io.krugosvet.dailydish.android.reminder.injection

import android.app.NotificationManager
import android.content.Context
import androidx.work.WorkManager
import io.krugosvet.dailydish.android.reminder.ReminderService
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationReceiver
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationService
import org.koin.dsl.module

val reminderModule = module {

    single {
        WorkManager.getInstance(get())
    }

    single {
        ReminderService(get(), get())
    }

    single {
        ReminderNotificationService(
            get(),
            get(),
            get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        )
    }

    single {
        ReminderNotificationReceiver(get(), get())
    }
}
