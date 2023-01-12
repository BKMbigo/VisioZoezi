package com.github.bkmbigo.visiozoezi.common.data.persisistence

import android.content.Context
import com.github.bkmbigo.visiozoezi.common.data.persistence.VisioZoeziDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(
    private val context: Context
) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(VisioZoeziDatabase.Schema, context, "VisioZoezi.db")
    }
}