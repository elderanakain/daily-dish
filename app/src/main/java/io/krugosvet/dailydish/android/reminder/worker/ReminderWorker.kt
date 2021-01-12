package io.krugosvet.dailydish.android.reminder.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.krugosvet.dailydish.android.reminder.notification.ReminderNotificationService
import io.krugosvet.dailydish.android.repository.meal.MealRepository
import io.krugosvet.dailydish.core.service.DateService
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

internal class ReminderWorker(
  context: Context,
  workerParams: WorkerParameters
) :
  CoroutineWorker(context, workerParams),
  KoinComponent {

  private val mealRepository: MealRepository by inject()
  private val reminderNotificationService: ReminderNotificationService by inject()
  private val dateString: DateService by inject()

  private val oneMonthAgo: Date
    get() = Calendar.getInstance()
      .apply { add(Calendar.MONTH, -1) }
      .time

  override suspend fun doWork(): Result {

    mealRepository.meals
      .first()
      .firstOrNull { meal ->
        with(dateString) {
          meal.lastCookingDate.defaultFormatDate().before(oneMonthAgo)
        }
      }
      ?.also { meal ->
        reminderNotificationService.sendReminderNotification(meal)
      }
      ?: return Result.failure()

    return Result.success()
  }

}
