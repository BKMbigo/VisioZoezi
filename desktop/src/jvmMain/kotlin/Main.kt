import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import com.github.bkmbigo.visiozoezi.common.data.persisistence.DatabaseDriverFactory
import com.github.bkmbigo.visiozoezi.common.data.persisistence.createDatabase
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.DatabaseExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.StatsRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.persistence.VisioZoeziDatabase
import com.github.bkmbigo.visiozoezi.common.data.repositories.ExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.settings.SettingsKeys
import com.github.bkmbigo.visiozoezi.common.data.settings.settings
import com.github.bkmbigo.visiozoezi.common.domain.repositories.ExerciseRepository
import com.github.bkmbigo.visiozoezi.common.domain.repositories.StatsRepository
import com.github.bkmbigo.visiozoezi.common.presentation.screens.home.HomeScreen
import com.github.bkmbigo.visiozoezi.common.presentation.theme.VisioZoeziTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Fakes the network call
 */

//val fakeNetworkExerciseRepository = object : NetworkExerciseRepository {
//    override suspend fun getAllExercises(
//        equipmentList: List<Equipment>,
//        bodyPartList: List<BodyPart>,
//        targetMuscleList: List<TargetMuscle>
//    ): List<Exercise> {
//        return listOf(
//            Exercise(
//                id = 661,
//                name = "Push Up",
//                gifUrl = "http://d205bpvrqc9yn1.cloudfront.net/0001.gif",
//                equipment = Equipment(
//                    id = 1,
//                    name = "Body Weight"
//                ),
//                bodyPart = BodyPart(
//                    id = 2,
//                    name = "arms"
//                ),
//                targetMuscle = TargetMuscle(
//                    id = 1,
//                    name = "abs"
//                )
//            )
//        )
//    }
//
//    override suspend fun getAllEquipment(): List<Equipment> {
//        return listOf(
//            Equipment(1, "Body Weight"),
//            Equipment(2, "Dumbbell"),
//            Equipment(3, "Bench Rest"),
//        )
//    }
//
//    override suspend fun getAllBodyParts(): List<BodyPart> {
//        return listOf(
//            BodyPart(1, "chest"),
//            BodyPart(2, "arm"),
//            BodyPart(3, "back"),
//        )
//    }
//
//    override suspend fun getAllTargetMuscles(): List<TargetMuscle> {
//        return listOf(
//            TargetMuscle(1, "abs"),
//            TargetMuscle(2, "biceps"),
//            TargetMuscle(3, "triceps"),
//        )
//    }
//}


fun main() = application {
    val customScope = CoroutineScope(Job())

    var database: VisioZoeziDatabase? = null
    var exerciseRepository: ExerciseRepository? = null
    var statsRepository: StatsRepository? = null

    customScope.launch {
        val db = createDatabase(DatabaseDriverFactory())
        database = db
        exerciseRepository = ExerciseRepositoryImpl(
            databaseExerciseRepository = DatabaseExerciseRepositoryImpl(db),
            //networkExerciseRepository = fakeNetworkExerciseRepository
        )
        statsRepository = StatsRepositoryImpl(db)
    }


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
            exerciseRepository?.let { exRep ->
                statsRepository?.let { statRep ->
                    Navigator(
                        HomeScreen(exRep, statRep)
                    )
                }
            } ?: Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Waiting for Database Creation")
            }

        }
    }
}
