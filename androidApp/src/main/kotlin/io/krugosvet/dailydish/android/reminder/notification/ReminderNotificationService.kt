package io.krugosvet.dailydish.android.reminder.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import io.krugosvet.dailydish.android.R
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotification.CookedTodayAction
import io.krugosvet.dailydish.android.ui.container.ContainerActivity
import io.krugosvet.dailydish.common.dto.Meal

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
    private val resources: Resources,
    private val notificationManager: NotificationManager,
) {

    private val onTapIntent
        get() = PendingIntent.getActivity(
            context,
            ReminderNotification.ID,
            Intent(context, ContainerActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT,
        )

    fun sendReminderNotification(meal: Meal) {
        if (!isChannelAdded()) {
            addChannel()
        }

        val notification = Notification.Builder(context, ReminderNotification.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_reminder)
            .setContentTitle(
                resources.getString(R.string.notification_title, meal.title),
            )
            .setContentText(
                resources.getString(R.string.notification_text, meal.title),
            )
            .setContentIntent(onTapIntent)
            .setAutoCancel(true)
            .addAction(
                Notification.Action
                    .Builder(
                        null,
                        resources.getString(R.string.meal_card_cooked_button),
                        createOnCookedTodayAction(meal.id),
                    )
                    .build(),
            )
            .build()

        notificationManager.notify(ReminderNotification.ID, notification)
    }

    fun closeReminder() {
        notificationManager.cancel(ReminderNotification.ID)
    }

    private fun createOnCookedTodayAction(mealId: String): PendingIntent =
        PendingIntent.getBroadcast(
            context,
            ReminderNotification.ID,
            Intent(CookedTodayAction.INTENT).apply {
                putExtra(CookedTodayAction.MEAL_ID_KEY, mealId)
            },
            PendingIntent.FLAG_ONE_SHOT,
        )

    private fun isChannelAdded(): Boolean =
        notificationManager.getNotificationChannel(ReminderNotification.CHANNEL_ID) != null

    private fun addChannel() {
        notificationManager
            .createNotificationChannel(
                NotificationChannel(
                    ReminderNotification.CHANNEL_ID,
                    resources.getString(R.string.notification_channel_title),
                    NotificationManager.IMPORTANCE_HIGH,
                ).apply {
                    enableVibration(true)
                },
            )
    }
}
