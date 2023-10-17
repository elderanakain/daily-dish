package io.krugosvet.dailydish.android.reminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.repository.MealRepository
import kotlinx.coroutines.runBlocking

class ReminderNotificationReceiver(
    private val mealRepository: MealRepository,
    private val reminderNotificationService: ReminderNotificationService,
) :
    BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val mealId = intent.getStringExtra(ReminderNotification.CookedTodayAction.MEAL_ID_KEY)

        if (mealId == null) {
            reminderNotificationService.closeReminder()
            return
        }

        runBlocking {
            val updatedMeal = mealRepository.get(mealId)
                .copy(lastCookingDate = currentDate)

            mealRepository.update(updatedMeal, null)
        }

        reminderNotificationService.closeReminder()
    }

}
