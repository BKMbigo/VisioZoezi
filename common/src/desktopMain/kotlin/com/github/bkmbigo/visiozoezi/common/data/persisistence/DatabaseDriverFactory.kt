package com.github.bkmbigo.visiozoezi.common.data.persisistence

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.bkmbigo.visiozoezi.common.data.persistence.VisioZoeziDatabase

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(schema: SqlSchema): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        VisioZoeziDatabase.Schema.create(driver)
        return driver
    }
}