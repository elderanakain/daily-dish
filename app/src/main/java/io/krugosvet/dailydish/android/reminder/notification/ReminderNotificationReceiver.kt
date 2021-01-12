package io.krugosvet.dailydish.android.reminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import io.krugosvet.dailydish.android.repository.meal.MealRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*

class ReminderNotificationReceiver(
  private val mealRepository: MealRepository,
  private val reminderNotificationService: ReminderNotificationService
) :
  BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    val mealId = intent.getStringExtra(ReminderNotification.CookedTodayAction.MEAL_ID_KEY)

    runBlocking {
      val updatedMeal = mealRepository.meals.first()
        .firstOrNull { it.id == mealId }
        ?.copy(lastCookingDate = Date().toString())
        ?: return@runBlocking

      mealRepository.update(updatedMeal, null)
    }

    reminderNotificationService.closeReminder()
  }

}
