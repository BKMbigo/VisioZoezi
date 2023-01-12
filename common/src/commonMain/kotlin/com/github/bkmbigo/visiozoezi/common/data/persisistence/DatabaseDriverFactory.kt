package com.github.bkmbigo.visiozoezi.common.data.persisistence

import com.github.bkmbigo.visiozoezi.common.data.persistence.VisioZoeziDatabase
import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DatabaseDriverFactory): VisioZoeziDatabase{
    val driver = driverFactory.createDriver()
    return VisioZoeziDatabase(driver)
}