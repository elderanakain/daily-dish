package io.krugosvet.dailydish.android

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import io.krugosvet.dailydish.android.reminder.ReminderService
import javax.inject.Inject
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber

@HiltAndroidApp
class DailyDishApplication :
    Application(),
    Configuration.Provider {

    @Inject
    lateinit var reminderService: ReminderService

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        reminderService.schedule()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(EntryPoints.get(this, WorkerEntryPoint::class.java).workerFactory())
            .build()

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WorkerEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }
}

val errorHandler = CoroutineExceptionHandler { _, error -> Timber.e(error) }
