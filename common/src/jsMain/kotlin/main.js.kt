import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.github.bkmbigo.visiozoezi.common.data.network.repositories.NetworkExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.persisistence.DatabaseDriverFactory
import com.github.bkmbigo.visiozoezi.common.data.persisistence.createDatabase
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.DatabaseExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.repositories.ExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.repositories.ExerciseRepository
import com.github.bkmbigo.visiozoezi.common.presentation.screens.home.HomeScreen
import com.github.bkmbigo.visiozoezi.common.presentation.theme.VisioZoeziTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    onWasmReady {
        BrowserViewportWindow("VisioZoezi") {
            var exerciseRepository by remember { mutableStateOf<ExerciseRepository?>(null) }
            val fullExerciseList = remember { MutableStateFlow<List<Exercise>>(emptyList()) }

            LaunchedEffect(Unit) {
                val db = createDatabase(DatabaseDriverFactory())
                val exRep = ExerciseRepositoryImpl(
                    databaseExerciseRepository = DatabaseExerciseRepositoryImpl(db),
                    networkExerciseRepository = NetworkExerciseRepositoryImpl(),
                    ioDispatcher = Dispatchers.Default
                )
                console.log("exrep is ready")
                exerciseRepository = exRep
                exRep.getAllExercises().stateIn(this, SharingStarted.Eagerly, emptyList()).collect{
                    fullExerciseList.value = it
                }
            }

            VisioZoeziTheme {
                exerciseRepository?.let {
                    HomeScreen(
                        exerciseRepository = it,
                        fullExerciseList = fullExerciseList,
                        modifier = Modifier.fillMaxSize(),
                        navigateToExerciseScreen = { exercise: Exercise, b: Boolean -> },
                        navigateToUserScreen = {},
                        navigateToFilterScreen = {},
                        ioDispatcher = Dispatchers.Default
                    )
                } ?: Column(Modifier.fillMaxSize()) {
                    Text("Preparing Database...")
                }
            }
        }
    }
}