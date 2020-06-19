package io.krugosvet.dailydish.android.reminder

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.krugosvet.dailydish.android.reminder.worker.ReminderWorker
import java.util.concurrent.TimeUnit

private const val REMINDER_WORK_ID = "reminder_work_id"

class ReminderService(
  context: Context
) {

  private val workManager = WorkManager.getInstance(context)

  private val constrains: Constraints
    get() = Constraints.Builder()
      .setRequiresBatteryNotLow(true)
      //.setRequiresDeviceIdle(true)
      .build()

  private val work: PeriodicWorkRequest
    get() = PeriodicWorkRequest
      .Builder(
        ReminderWorker::class.java,
        1, TimeUnit.DAYS
      )
      .setConstraints(constrains)
      .build()

  fun schedule() {
    workManager
      .enqueueUniquePeriodicWork(
        REMINDER_WORK_ID,
        ExistingPeriodicWorkPolicy.REPLACE,
        work
      )
  }

}
