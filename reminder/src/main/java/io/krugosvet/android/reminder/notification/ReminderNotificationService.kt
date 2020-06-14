package io.krugosvet.android.reminder.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.krugosvet.dailydish.android.reminder.R
import io.krugosvet.dailydish.android.repository.meal.Meal
import io.krugosvet.dailydish.core.service.ResourceService

private object Notification {

  const val ID = 1000
  const val CHANNEL_ID = "dailyDishChannel"
}

internal class ReminderNotificationService(
  private val context: Context,
  private val resourceService: ResourceService
) {

  fun sendReminderNotification(meal: Meal) {

    addChannel()

    val notification = NotificationCompat.Builder(context, Notification.CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_notification_reminder)
      .setContentTitle(
        resourceService.getString(R.string.notification_title, meal.title)
      )
      .setContentText(
        resourceService.getString(R.string.notification_text, meal.title)
      )
      .setPriority(NotificationCompat.PRIORITY_HIGH)
      .build()

    NotificationManagerCompat
      .from(context)
      .notify(Notification.ID, notification)
  }

  private fun addChannel() {
    (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?)
      ?.createNotificationChannel(
        NotificationChannel(
          Notification.CHANNEL_ID,
          resourceService.getString(R.string.notification_channel_title),
          NotificationManager.IMPORTANCE_HIGH
        )
      )
  }
}
