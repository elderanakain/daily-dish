package io.krugosvet.dailydish.android.reminder.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotification.CookedTodayAction
import io.krugosvet.dailydish.android.repository.meal.Meal
import io.krugosvet.dailydish.android.screen.container.view.ContainerActivity
import io.krugosvet.dailydish.core.service.ResourceService

object ReminderNotification {

  const val ID = 1000
  const val CHANNEL_ID = "dailyDishChannel"

  object CookedTodayAction {
    const val INTENT = "io.krugosvet.dailydish.android.reminder.notification.COOKED_TODAY"
    const val MEAL_ID_KEY = "mealId"
  }
}

class ReminderNotificationService(
  private val context: Context,
  private val resourceService: ResourceService,
  private val notificationManager: NotificationManagerCompat
) {

  private val onTapIntent
    get() = PendingIntent.getActivity(
      context,
      ReminderNotification.ID,
      Intent(context, ContainerActivity::class.java),
      PendingIntent.FLAG_UPDATE_CURRENT
    )

  fun sendReminderNotification(meal: Meal) {

    if (!isChannelAdded()) {
      addChannel()
    }

    val notification = NotificationCompat.Builder(context, ReminderNotification.CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_notification_reminder)
      .setContentTitle(
        resourceService.getString(R.string.notification_title, meal.title)
      )
      .setContentText(
        resourceService.getString(R.string.notification_text, meal.title)
      )
      .setPriority(NotificationCompat.PRIORITY_LOW)
      .setContentIntent(onTapIntent)
      .setAutoCancel(true)
      .setDefaults(NotificationCompat.DEFAULT_ALL)
      .addAction(
        NotificationCompat.Action(
          null,
          resourceService.getString(R.string.meal_card_cooked_button),
          createOnCookedTodayAction(meal.id)
        )
      )
      .build()

    NotificationManagerCompat
      .from(context)
      .notify(ReminderNotification.ID, notification)
  }

  fun closeReminder() {
    notificationManager.cancel(ReminderNotification.ID)
  }

  private fun createOnCookedTodayAction(mealId: Long) =
    PendingIntent.getBroadcast(
      context, ReminderNotification.ID,
      Intent(CookedTodayAction.INTENT).apply {
        putExtra(CookedTodayAction.MEAL_ID_KEY, mealId)
      },
      PendingIntent.FLAG_ONE_SHOT
    )

  private fun isChannelAdded(): Boolean =
    notificationManager.getNotificationChannel(ReminderNotification.CHANNEL_ID) == null

  private fun addChannel() {
    notificationManager
      .createNotificationChannel(
        NotificationChannel(
          ReminderNotification.CHANNEL_ID,
          resourceService.getString(R.string.notification_channel_title),
          NotificationManager.IMPORTANCE_HIGH
        )
      )
  }
}
