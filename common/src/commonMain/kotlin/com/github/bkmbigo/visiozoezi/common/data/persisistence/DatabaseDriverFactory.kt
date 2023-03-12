package com.github.bkmbigo.visiozoezi.common.data.persisistence

import app.cash.sqldelight.ColumnAdapter
import com.github.bkmbigo.visiozoezi.common.data.persistence.VisioZoeziDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import com.github.bkmbigo.visiozoezi.common.data.persistence.Exercise_stat

expect class DatabaseDriverFactory {
    suspend fun createDriver(schema: SqlSchema): SqlDriver
}

suspend fun createDatabase(driverFactory: DatabaseDriverFactory): VisioZoeziDatabase {
    val driver = driverFactory.createDriver(VisioZoeziDatabase.Schema)
    VisioZoeziDatabase.Schema.create(driver).await()
    return VisioZoeziDatabase(
        driver,
        Exercise_stat.Adapter(
            repetitionsAdapter = object : ColumnAdapter<Int, Long> {
                override fun decode(databaseValue: Long): Int =
                    databaseValue.toInt()

                override fun encode(value: Int): Long =
                    value.toLong()

            }
        )
    )
}