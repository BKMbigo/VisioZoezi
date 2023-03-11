package com.github.bkmbigo.visiozoezi.common.data.persisistence

import com.github.bkmbigo.visiozoezi.common.data.persistence.VisioZoeziDatabase
import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    suspend fun createDriver(schema: SqlDriver.Schema): SqlDriver
}

suspend fun createDatabase(driverFactory: DatabaseDriverFactory): VisioZoeziDatabase{
    val driver = driverFactory.createDriver(VisioZoeziDatabase.Schema)
    return VisioZoeziDatabase(driver)
}