package io.krugosvet.dailydish.android.reminder.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationService
import io.krugosvet.dailydish.common.core.currentDate
import io.krugosvet.dailydish.common.repository.MealRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first
import kotlinx.datetime.monthsUntil

@HiltWorker
class ReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
) :
    CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var mealRepository: MealRepository

    @Inject
    lateinit var reminderNotificationService: ReminderNotificationService

    override suspend fun doWork(): Result =
        mealRepository.observe().first()
            .firstOrNull { meal ->
                meal.lastCookingDate.monthsUntil(currentDate) > 1
            }
            ?.let { meal ->
                reminderNotificationService.sendReminderNotification(meal)
                Result.success()
            }
            ?: Result.failure()
}
