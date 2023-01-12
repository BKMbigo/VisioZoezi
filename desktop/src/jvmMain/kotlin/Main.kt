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

//val exerciseRepository = ExerciseRepositoryImpl(
//    databaseExerciseRepository = DatabaseExerciseRepositoryImpl(database)
//)

val exerciseRepository = object: ExerciseRepository {
    override suspend fun searchExercise(
        query: String?,
        equipment: Equipment?,
        bodyPart: BodyPart?,
        targetMuscle: TargetMuscle?
    ): List<Exercise> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllExercises(): Flow<List<Exercise>> {
        return flowOf(
            listOf(
                Exercise(
                    2,
                    "My Exercise",
                    "http://d205bpvrqc9yn1.cloudfront.net/0001.gif",
                    Equipment(2, "Barbell"),
                    BodyPart(3, "Chest"),
                    TargetMuscle(2, "Abs")
                ),
                Exercise(
                    6,
                    "My Second Exercise",
                    "http://d205bpvrqc9yn1.cloudfront.net/0041.gif",
                    Equipment(2, "Dumbbell"),
                    BodyPart(3, "Upper Arm"),
                    TargetMuscle(2, "Abs")
                )
            )
        )
    }

    override suspend fun getAllEquipment(): Flow<List<Equipment>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllBodyParts(): Flow<List<BodyPart>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTargetMuscle(): Flow<List<TargetMuscle>> {
        TODO("Not yet implemented")
    }

}

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
