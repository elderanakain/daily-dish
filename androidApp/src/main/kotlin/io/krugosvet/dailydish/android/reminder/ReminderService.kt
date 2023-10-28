package io.krugosvet.dailydish.android.reminder

import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import io.krugosvet.dailydish.android.reminder.worker.ReminderWorker
import io.krugosvet.dailydish.android.service.PreferenceService
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val REMINDER_WORK_ID = "reminder_work_id"

class ReminderService @Inject constructor(
    private val workManager: WorkManager,
    private val preferenceService: PreferenceService,
) {

    private val constrains: Constraints
        get() = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresDeviceIdle(true)
            .build()

    private val work: PeriodicWorkRequest
        get() = PeriodicWorkRequest
            .Builder(ReminderWorker::class.java, 1, TimeUnit.DAYS)
            .setConstraints(constrains)
            .build()

    fun schedule() = GlobalScope.launch(Dispatchers.IO) {
        preferenceService.isReminderEnabled
            .collect { isEnabled ->
                if (isEnabled) startWorker() else stopWorker()
            }
    }

    private fun stopWorker() {
        workManager.cancelUniqueWork(REMINDER_WORK_ID)
    }

    private fun startWorker() {
        workManager.enqueueUniquePeriodicWork(
            REMINDER_WORK_ID,
            ExistingPeriodicWorkPolicy.KEEP,
            work,
        )
    }
}
