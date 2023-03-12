package com.github.bkmbigo.visiozoezi.common.data.persisistence

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.sqljs.initSqlDriver
import kotlinx.coroutines.await

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(schema: SqlSchema): SqlDriver {
        console.log("Creating Driver....")
        val driver = initSqlDriver(schema).await()
        console.log("Driver created")
        return driver
    }
}