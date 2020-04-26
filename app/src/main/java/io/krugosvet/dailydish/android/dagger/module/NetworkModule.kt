package io.krugosvet.dailydish.android.dagger.module

import com.serjltt.moshi.adapters.DeserializeOnly
import com.serjltt.moshi.adapters.SerializeNulls
import com.serjltt.moshi.adapters.Wrapped
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import io.krugosvet.dailydish.android.network.MealService
import io.krugosvet.dailydish.android.network.MealServicePipe
import io.krugosvet.dailydish.android.network.MealServicePipeImpl
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import javax.inject.Singleton

@Module
class NetworkModule {

  @Singleton
  @Provides
  fun provideDailyDishService(okHttpClient: OkHttpClient): MealService =
    Retrofit.Builder()
      .baseUrl("https://test.com")
      .callFactory(okHttpClient)
      .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
      .addCallAdapterFactory(getRxJavaFactory())
      .build().create(MealService::class.java)

  @Singleton
  @Provides
  fun provideMealServicePipe(mealService: MealService): MealServicePipe =
    MealServicePipeImpl(mealService)

  @Singleton
  @Provides
  fun provideOkHttpClient(): OkHttpClient =
    OkHttpClient.Builder()
      .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
      .build()

  private fun getRxJavaFactory() =
    RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

  private fun getMoshi() =
    Moshi.Builder()
      .add(SerializeNulls.ADAPTER_FACTORY)
      .add(DeserializeOnly.ADAPTER_FACTORY)
      .add(Wrapped.ADAPTER_FACTORY)
      .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
      .build()

}
