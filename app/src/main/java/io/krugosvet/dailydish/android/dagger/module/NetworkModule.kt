package io.krugosvet.dailydish.android.dagger.module

import android.support.annotation.NonNull
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import dagger.Module
import dagger.Provides
import io.krugosvet.dailydish.android.BuildConfig
import io.krugosvet.dailydish.android.ibm.appId.AuthTokenManager
import io.krugosvet.dailydish.android.network.MealService
import io.krugosvet.dailydish.android.network.MealServicePipe
import io.krugosvet.dailydish.android.network.MealServicePipeImpl
import io.krugosvet.dailydish.android.utils.DEFAULT_DATE_FORMAT
import io.krugosvet.dailydish.android.utils.defaultFormatDate
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton


@Module
class NetworkModule {

    @Singleton
    @NonNull
    @Provides
    fun provideDailyDishService(okHttpClient: OkHttpClient): MealService = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(getGson())
            .callFactory(okHttpClient)
            .addCallAdapterFactory(getRxJavaFactory())
            .build().create(MealService::class.java)

    @Singleton
    @NonNull
    @Provides
    fun provideMealServicePipe(mealService: MealService, authTokenManager: AuthTokenManager):
            MealServicePipe = MealServicePipeImpl(mealService, authTokenManager)

    @Singleton
    @NonNull
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    private fun getGson() = GsonConverterFactory.create(
            GsonBuilder()
                    .registerTypeAdapter(Date::class.java, JsonDeserializer<Date> { json, _, _ ->
                        defaultFormatDate(json.asString)
                    })
                    .setDateFormat(DEFAULT_DATE_FORMAT)
                    .create())

    private fun getRxJavaFactory() = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
}
