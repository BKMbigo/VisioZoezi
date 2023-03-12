package com.github.bkmbigo.visiozoezi.common.ml.classifier

import com.github.bkmbigo.visiozoezi.common.ml.classifier.PoseClassificationResult
import com.github.bkmbigo.visiozoezi.common.ml.classifier.PoseClassifier
import com.github.bkmbigo.visiozoezi.common.ml.classifier.models.PoseClassificationModel
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseResult
import kotlinx.datetime.Clock
import org.jetbrains.kotlinx.dl.api.core.Sequential
import org.jetbrains.kotlinx.dl.api.core.loss.Losses
import org.jetbrains.kotlinx.dl.api.core.metric.Metrics
import org.jetbrains.kotlinx.dl.api.core.optimizer.Adam
import java.io.File

actual class PoseClassifier(
    private val model: Sequential
) {
    actual fun analyze(
        poseResult: PoseResult,
        onAnalysisComplete: (PoseClassificationResult) -> Unit
    ) {

        val start = Clock.System.now()
        val prediction = model.predict(
            poseResult.points.map { listOf(it.x, it.y) }.flatten().toFloatArray()
        )
        val end = Clock.System.now()

    }

    actual fun close() {
        model.close()
    }

    actual class Builder {

        actual fun build(model: PoseClassificationModel): PoseClassifier {
            // Model used was trained in the Training Layer
            //
            val modelConfig = File("${MODELS_PATH}${model.savedModelFolderName}/model.json")
            val weights = File("${MODELS_PATH}${model.savedModelFolderName}/weights.h5")

            val model = Sequential.loadModelConfiguration(modelConfig)
            model.compile(Adam(), Losses.BINARY_CROSSENTROPY, Metrics.ACCURACY)
            model.loadWeights(weights)

            return PoseClassifier(model)
        }

        companion object {
            const val MODELS_PATH =
                "/mnt/Programming/Projects/Programming/JavaVirtualMachine/Kotlin/PersonalProjects/KotlinMultiplatform/VisioZoezi/common/src/desktopMain/resources/models/"
        }

    }

}