package com.github.bkmbigo.visiozoezi.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.github.bkmbigo.visiozoezi.common.data.persisistence.DatabaseDriverFactory
import com.github.bkmbigo.visiozoezi.common.data.persisistence.createDatabase
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.DatabaseExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.StatsRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.repositories.ExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.settings.SettingsKeys
import com.github.bkmbigo.visiozoezi.common.data.settings.settings
import com.github.bkmbigo.visiozoezi.common.domain.repositories.ExerciseRepository
import com.github.bkmbigo.visiozoezi.common.domain.repositories.StatsRepository
import com.github.bkmbigo.visiozoezi.common.presentation.screens.home.HomeScreenNavigator
import com.github.bkmbigo.visiozoezi.common.presentation.theme.VisioZoeziTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var exerciseRepository by remember { mutableStateOf<ExerciseRepository?>(null) }
            var statsRepository by remember { mutableStateOf<StatsRepository?>(null) }

            LaunchedEffect(Unit) {
                val db = createDatabase(
                    DatabaseDriverFactory(this@MainActivity)
                )
                exerciseRepository = ExerciseRepositoryImpl(
                    DatabaseExerciseRepositoryImpl(db, Dispatchers.IO),
                    ioDispatcher = Dispatchers.IO
                )
                statsRepository = StatsRepositoryImpl(db, Dispatchers.IO)
            }

            VisioZoeziTheme(
                if (settings.getBoolean(
                        SettingsKeys.FORCE_DARK_THEME,
                        false
                    )
                ) true else isSystemInDarkTheme()
            ) {
                if (exerciseRepository != null && statsRepository != null) {
                    Navigator(
                        screen = HomeScreenNavigator(exerciseRepository!!, statsRepository!!),
                    ) {
                        SlideTransition(it)
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Awaiting Database Creation")
                    }
                }
            }
        }
    }
}