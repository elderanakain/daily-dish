package io.krugosvet.dailydish.common.repository.db

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

internal actual fun createDb(): Database {
    val driver = NativeSqliteDriver(Database.Schema, "dailydish.db")

    return Database(driver)
}