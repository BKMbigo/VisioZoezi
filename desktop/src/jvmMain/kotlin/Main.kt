import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import cafe.adriel.voyager.navigator.Navigator
import com.github.bkmbigo.visiozoezi.common.data.persisistence.DatabaseDriverFactory
import com.github.bkmbigo.visiozoezi.common.data.persisistence.createDatabase
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.DatabaseExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.persisistence.repositories.StatsRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.repositories.ExerciseRepositoryImpl
import com.github.bkmbigo.visiozoezi.common.data.repositories.NetworkExerciseRepository
import com.github.bkmbigo.visiozoezi.common.data.settings.SettingsKeys
import com.github.bkmbigo.visiozoezi.common.data.settings.settings
import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle
import com.github.bkmbigo.visiozoezi.common.presentation.screens.home.HomeScreen
import com.github.bkmbigo.visiozoezi.common.presentation.theme.VisioZoeziTheme


val database = createDatabase(DatabaseDriverFactory())

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

val exerciseRepository = ExerciseRepositoryImpl(
    databaseExerciseRepository = DatabaseExerciseRepositoryImpl(database),
    //networkExerciseRepository = fakeNetworkExerciseRepository
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
