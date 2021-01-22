package io.krugosvet.dailydish.common.repository.db

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.krugosvet.dailydish.common.repository.db.Database

public lateinit var appContext: Context

internal actual fun createDb(): Database {
  val driver = AndroidSqliteDriver(Database.Schema, appContext, "dailydish.db")

  return Database(driver)
}
