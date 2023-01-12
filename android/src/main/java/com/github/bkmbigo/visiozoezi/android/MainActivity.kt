package com.github.bkmbigo.visiozoezi.android

import com.github.bkmbigo.visiozoezi.common.App
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import cafe.adriel.voyager.navigator.Navigator
import com.github.bkmbigo.visiozoezi.common.data.persisistence.DatabaseDriverFactory
import com.github.bkmbigo.visiozoezi.common.data.persisistence.createDatabase
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.DatabaseExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.StatsRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.repositories.ExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.domain.repositories.ExerciseRepository
import com.github.bkmbigo.visiozoezi.common.presentation.screens.home.HomeScreen
import com.github.bkmbigo.visiozoezi.common.presentation.theme.VisioZoeziTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = createDatabase(
            DatabaseDriverFactory(this)
        )

        setContent {
            val exerciseRepository = ExerciseRepositoryImpl(
                databaseExerciseRepository = DatabaseExerciseRepositoryImpl(database)
            )

            val statsRepository = StatsRepositoryImpl(database)

            VisioZoeziTheme {
                Navigator(
                    screen = HomeScreen(exerciseRepository, statsRepository),
                )
            }
        }
    }
}