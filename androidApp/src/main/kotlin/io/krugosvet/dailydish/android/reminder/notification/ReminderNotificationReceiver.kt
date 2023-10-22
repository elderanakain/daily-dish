package io.krugosvet.dailydish.android.reminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.repository.MealRepository
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReminderNotificationReceiver :
    BroadcastReceiver(),
    KoinComponent {

    private val mealRepository: MealRepository by inject()
    private val reminderNotificationService: ReminderNotificationService by inject()

    override fun onReceive(context: Context, intent: Intent) {
        val mealId = intent.getStringExtra(ReminderNotification.CookedTodayAction.MEAL_ID_KEY)

        if (mealId == null) {
            reminderNotificationService.closeReminder()
            return
        }

        runBlocking {
            val updatedMeal = mealRepository.get(mealId)
                .copy(lastCookingDate = currentDate)

            mealRepository.update(updatedMeal)
        }

        reminderNotificationService.closeReminder()
    }
}
