import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import com.github.bkmbigo.visiozoezi.common.App
import com.github.bkmbigo.visiozoezi.common.data.persisistence.DatabaseDriverFactory
import com.github.bkmbigo.visiozoezi.common.data.persisistence.createDatabase
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.DatabaseExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.StatsRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.repositories.ExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle
import com.github.bkmbigo.visiozoezi.common.domain.repositories.ExerciseRepository
import com.github.bkmbigo.visiozoezi.common.presentation.screens.home.HomeScreen
import com.github.bkmbigo.visiozoezi.common.presentation.theme.VisioZoeziTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


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
        VisioZoeziTheme {
            Navigator(
                HomeScreen(exerciseRepository, statsRepository)
            )
        }
    }
}
