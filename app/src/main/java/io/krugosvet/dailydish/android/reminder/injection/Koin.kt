package io.krugosvet.dailydish.android.reminder.injection

import android.content.Context
import androidx.core.app.NotificationManagerCompat
import io.krugosvet.dailydish.android.reminder.ReminderService
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationService
import io.krugosvet.dailydish.core.service.ResourceService
import org.koin.dsl.module

@Suppress("RemoveExplicitTypeArguments")
val reminderModule = module {

  single<ReminderService> {
    ReminderService(
      get<Context>()
    )
  }

  single<NotificationManagerCompat> {
    NotificationManagerCompat.from(
      get<Context>()
    )
  }

  single<ReminderNotificationService> {
    ReminderNotificationService(
      get<Context>(),
      get<ResourceService>(),
      get<NotificationManagerCompat>()
    )
  }
}
