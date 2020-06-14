package io.krugosvet.android.reminder.injection

import android.content.Context
import io.krugosvet.android.reminder.ReminderService
import io.krugosvet.android.reminder.notification.ReminderNotificationService
import io.krugosvet.dailydish.core.service.ResourceService
import org.koin.dsl.module

@Suppress("RemoveExplicitTypeArguments")
val reminderModule = module {

  single<ReminderService> {
    ReminderService(
      get<Context>()
    )
  }

  single<ReminderNotificationService> {
    ReminderNotificationService(
      get<Context>(),
      get<ResourceService>()
    )
  }
}
