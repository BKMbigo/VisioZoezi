package com.github.bkmbigo.visiozoezi.common.ml.classifier

import com.github.bkmbigo.visiozoezi.common.ml.classifier.models.PoseClassificationModel
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseResult
import kotlinx.datetime.Clock
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.common.io.ClassPathResource
import org.nd4j.linalg.api.buffer.DataType
import org.nd4j.linalg.factory.Nd4j
import java.io.File

actual class PoseClassifier(
    private val model: MultiLayerNetwork
) {
    actual fun analyze(
        poseResult: PoseResult,
        onAnalysisComplete: (PoseClassificationResult) -> Unit
    ) {

        val float = poseResult.points.map { listOf(it.x, it.y) }.flatten().toFloatArray()
        val features = Nd4j.zeros(intArrayOf(34, 1), DataType.FLOAT)
        float.forEachIndexed { index, fl ->
            features.putScalar(index.toLong(), fl)
        }

        val start = Clock.System.now()
        val prediction = model.output(
            features
        )
        val end = Clock.System.now()

    }

    actual fun close() {
        model.close()
    }

    actual class Builder {

        actual fun build(model: PoseClassificationModel): PoseClassifier {

            val modelConfig = File("${MODELS_PATH}${model.savedModelFolderName}/model.json")
            val weights = File("${MODELS_PATH}${model.savedModelFolderName}/weights.h5")

            val simpleMLP =
                ClassPathResource("weights").file.path
            val model = KerasModelImport.importKerasSequentialModelAndWeights(simpleMLP)

            return PoseClassifier(model)
        }

        companion object {
            const val PROJECT_PATH = "/mnt/Programming/Projects/Programming/JavaVirtualMachine/Kotlin/PersonalProjects/KotlinMultiplatform/VisioZoezi/"
            const val MODELS_PATH = "/src/desktopMain/resources/models/"
        }

    }

}