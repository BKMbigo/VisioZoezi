package com.github.bkmbigo.visiozoezi.common.data.persisistence

import com.github.bkmbigo.visiozoezi.common.data.persistence.VisioZoeziDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(schema: SqlDriver.Schema): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        VisioZoeziDatabase.Schema.create(driver)
        return driver
    }
}