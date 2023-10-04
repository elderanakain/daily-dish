package io.krugosvet.dailydish.common.repository.db

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

internal actual fun createDb(): Database =
    HikariConfig()
        .apply {
            jdbcUrl = System.getenv(DB_URL)
            username = System.getenv(DB_USER)
            password = System.getenv(DB_PASS)
            isAutoCommit = true
            transactionIsolation = DB_ISOLATION
            maximumPoolSize = 1
        }
        .let { config ->
            HikariDataSource(config).asJdbcDriver()
        }
        .let { driver ->
            Database.Schema.create(driver)

            Database(driver)
        }

private const val DB_URL = "JDBC_DATABASE_URL"
private const val DB_USER = "JDBC_DATABASE_USERNAME"
private const val DB_PASS = "JDBC_DATABASE_PASSWORD"
private const val DB_ISOLATION = "TRANSACTION_SERIALIZABLE"
