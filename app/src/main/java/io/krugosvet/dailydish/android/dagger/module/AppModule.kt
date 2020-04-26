package io.krugosvet.dailydish.android.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class AppModule(
  private val appContext: Context
) {

  @Provides
  @Singleton
  fun provideContext() = appContext

  @Provides
  fun provideRealm(): Realm = Realm.getDefaultInstance()
}
