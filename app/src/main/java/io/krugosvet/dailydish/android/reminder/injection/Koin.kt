package io.krugosvet.dailydish.android.reminder.injection

import androidx.core.app.NotificationManagerCompat
import io.krugosvet.dailydish.android.reminder.ReminderService
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationReceiver
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationService
import org.koin.dsl.module

val reminderModule = module {

  single {
    ReminderService(get())
  }

  single {
    NotificationManagerCompat.from(get())
  }

  single {
    ReminderNotificationService(get(), get(), get())
  }

  single {
    ReminderNotificationReceiver(get(), get())
  }
}
