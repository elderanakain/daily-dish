package io.krugosvet.dailydish.android.dagger.module

import android.content.Context
import android.support.annotation.NonNull
import dagger.Module
import dagger.Provides
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManager
import io.krugosvet.dailydish.android.network.BASE_URL
import io.krugosvet.dailydish.android.network.MealService
import io.krugosvet.dailydish.android.network.MealServicePipe
import io.krugosvet.dailydish.android.network.MealServicePipeImpl
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @NonNull
    @Provides
    fun provideDailyDishService(okHttpClient: OkHttpClient): MealService = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .callFactory(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build().create(MealService::class.java)

    @Singleton
    @NonNull
    @Provides
    fun provideMealServicePipe(context: Context, mealService: MealService,
                               authTokenManager: AuthTokenManager): MealServicePipe =
            MealServicePipeImpl(context, mealService, authTokenManager)

    @Singleton
    @NonNull
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
}
