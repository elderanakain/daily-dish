package io.krugosvet.dailydish.android.di

import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import androidx.core.content.getSystemService
import androidx.preference.PreferenceManager
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.krugosvet.dailydish.android.service.PreferenceService
import io.krugosvet.dailydish.common.Environment
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun provideCommonEnvironment(): Environment = Environment.init()

    @Provides
    fun provideMealRepository(env: Environment) = env.mealRepository

    @Provides
    fun provideAddMealUseCase(env: Environment) = env.addMealUseCase

    @Provides
    fun provideDeleteMealUseCase(env: Environment) = env.deleteMealUseCase

    @Provides
    fun provideSetCurrentTimeUseCase(env: Environment) = env.setCurrentTimeToMealUseCase

    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources =
        context.resources

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    fun providePreferences(
        @ApplicationContext context: Context,
        resources: Resources
    ): PreferenceService =
        PreferenceService(PreferenceManager.getDefaultSharedPreferences(context), resources)

    @Provides
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager =
        context.getSystemService()!!
}
