package com.github.bkmbigo.visiozoezi.common.data.persisistence

import android.content.Context
import com.github.bkmbigo.visiozoezi.common.data.persistence.VisioZoeziDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(
    private val context: Context
) {
    actual suspend fun createDriver(schema: SqlDriver.Schema): SqlDriver {
        return AndroidSqliteDriver(VisioZoeziDatabase.Schema, context, "VisioZoezi.db")
    }
}