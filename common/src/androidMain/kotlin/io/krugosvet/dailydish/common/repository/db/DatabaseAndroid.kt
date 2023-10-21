package io.krugosvet.dailydish.common.repository.db

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

public lateinit var appContext: Context

internal actual fun createDb(): Database {
    val driver = AndroidSqliteDriver(Database.Schema, appContext, "dailydish.db")

    return Database(driver)
}
