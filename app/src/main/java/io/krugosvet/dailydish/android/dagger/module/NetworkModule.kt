package io.krugosvet.dailydish.android.dagger.module

import com.serjltt.moshi.adapters.*
import com.squareup.moshi.*
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.*
import io.krugosvet.dailydish.android.network.*
import io.reactivex.schedulers.*
import okhttp3.*
import okhttp3.logging.*
import retrofit2.*
import retrofit2.adapter.rxjava2.*
import retrofit2.converter.moshi.*
import java.util.*
import javax.inject.*

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
