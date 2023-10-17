package io.krugosvet.dailydish.android.reminder.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationService
import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.repository.MealRepository
import kotlinx.datetime.monthsUntil
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) :
    CoroutineWorker(context, workerParams),
    KoinComponent {

    private val mealRepository: MealRepository by inject()
    private val reminderNotificationService: ReminderNotificationService by inject()

    override suspend fun doWork(): Result =
        mealRepository.meals
            .firstOrNull { meal ->
                meal.lastCookingDate.monthsUntil(currentDate) > 1
            }
            ?.let { meal ->
                reminderNotificationService.sendReminderNotification(meal)
                Result.success()
            }
            ?: Result.failure()

}
