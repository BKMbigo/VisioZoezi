import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import com.github.bkmbigo.visiozoezi.common.data.persisistence.DatabaseDriverFactory
import com.github.bkmbigo.visiozoezi.common.data.persisistence.createDatabase
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.DatabaseExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.StatsRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.repositories.ExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.settings.SettingsKeys
import com.github.bkmbigo.visiozoezi.common.data.settings.settings
import com.github.bkmbigo.visiozoezi.common.presentation.screens.home.HomeScreen
import com.github.bkmbigo.visiozoezi.common.presentation.theme.VisioZoeziTheme


val database = createDatabase(DatabaseDriverFactory())

val exerciseRepository = ExerciseRepositoryImpl(
    databaseExerciseRepository = DatabaseExerciseRepositoryImpl(database)
)

val statsRepository = StatsRepositoryImpl(database)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "VisioZoezi"
    ) {
        VisioZoeziTheme(
            if (settings.getBoolean(
                    SettingsKeys.FORCE_DARK_THEME,
                    false
                )
            ) true else isSystemInDarkTheme()
        ) {
            Navigator(
                HomeScreen(exerciseRepository, statsRepository)
            )
        }
    }
}
