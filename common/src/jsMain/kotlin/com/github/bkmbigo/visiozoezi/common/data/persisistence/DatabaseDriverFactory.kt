package com.github.bkmbigo.visiozoezi.common.data.persisistence

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.sqljs.initSqlDriver
import kotlinx.coroutines.await

actual class DatabaseDriverFactory {
    actual suspend fun createDriver(schema: SqlDriver.Schema): SqlDriver {
        return initSqlDriver(schema).await()
    }
}