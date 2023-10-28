package io.krugosvet.dailydish.android

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import io.krugosvet.dailydish.android.reminder.ReminderService
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

@HiltAndroidApp
class DailyDishApplication :
    Application(), Configuration.Provider {

    @Inject
    lateinit var reminderService: ReminderService

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        reminderService.schedule()
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}

val errorHandler = CoroutineExceptionHandler { _, error -> Timber.e(error) }
