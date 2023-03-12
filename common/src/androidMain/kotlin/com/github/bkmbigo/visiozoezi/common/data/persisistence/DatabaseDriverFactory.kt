package com.github.bkmbigo.visiozoezi.common.data.persisistence

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

actual class DatabaseDriverFactory(private val context: Context) {
    actual suspend fun createDriver(schema: SqlSchema): SqlDriver {
        return AndroidSqliteDriver(schema, context, "VisioZoezi.db")
    }
}