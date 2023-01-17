package com.github.bkmbigo.visiozoezi.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.github.bkmbigo.visiozoezi.common.data.persisistence.DatabaseDriverFactory
import com.github.bkmbigo.visiozoezi.common.data.persisistence.createDatabase
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.DatabaseExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.StatsRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.repositories.ExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.settings.SettingsKeys
import com.github.bkmbigo.visiozoezi.common.data.settings.settings
import com.github.bkmbigo.visiozoezi.common.presentation.screens.home.HomeScreen
import com.github.bkmbigo.visiozoezi.common.presentation.theme.VisioZoeziTheme

class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalAnimationApi::class)
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

            VisioZoeziTheme(
                if (settings.getBoolean(
                        SettingsKeys.FORCE_DARK_THEME,
                        false
                    )
                ) true else isSystemInDarkTheme()
            ) {
                Navigator(
                    screen = HomeScreen(exerciseRepository, statsRepository),
                ) {
                    SlideTransition(it)
                }
            }
        }
    }
}