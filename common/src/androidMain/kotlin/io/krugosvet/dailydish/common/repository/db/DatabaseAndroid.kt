package io.krugosvet.dailydish.common.repository.db

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.krugosvet.dailydish.common.appContext

internal actual fun createDb(): Database {
    val driver = AndroidSqliteDriver(Database.Schema, appContext, "dailydish.db")

    return Database(driver)
}
