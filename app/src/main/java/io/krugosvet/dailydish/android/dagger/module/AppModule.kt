package io.krugosvet.dailydish.android.dagger.module

import android.content.*
import android.support.annotation.*
import dagger.*
import io.realm.*
import javax.inject.*

@Module
class AppModule(private val appContext: Context) {

  @Provides
  @NonNull
  @Singleton
  fun provideContext() = appContext

  @Provides
  @NonNull
  fun provideRealm(): Realm {
    return Realm.getDefaultInstance()
  }
}
