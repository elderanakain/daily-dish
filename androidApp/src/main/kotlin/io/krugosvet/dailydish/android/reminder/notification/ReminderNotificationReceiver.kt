package io.krugosvet.dailydish.android.reminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.repository.MealRepository
import javax.inject.Inject
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class ReminderNotificationReceiver :
    BroadcastReceiver() {

    @Inject
    lateinit var mealRepository: MealRepository

    @Inject
    lateinit var reminderNotificationService: ReminderNotificationService

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
