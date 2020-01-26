package io.krugosvet.dailydish.android.dagger.module

import android.support.annotation.*
import com.google.gson.*
import dagger.*
import io.krugosvet.dailydish.android.*
import io.krugosvet.dailydish.android.ibm.appId.*
import io.krugosvet.dailydish.android.network.*
import io.krugosvet.dailydish.android.utils.*
import io.reactivex.schedulers.*
import okhttp3.*
import okhttp3.logging.*
import retrofit2.*
import retrofit2.adapter.rxjava2.*
import retrofit2.converter.gson.*
import java.util.*
import javax.inject.*


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
