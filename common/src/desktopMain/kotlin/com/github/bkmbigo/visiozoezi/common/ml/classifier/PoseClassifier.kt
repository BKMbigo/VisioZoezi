package com.github.bkmbigo.visiozoezi.common.ml.classifier

import com.github.bkmbigo.visiozoezi.common.ml.classifier.models.PoseClassificationModel
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseResult
import kotlinx.datetime.Clock
import org.jetbrains.kotlinx.dl.api.inference.TensorFlowInferenceModel
import java.io.File

actual class PoseClassifier(
    private val model: TensorFlowInferenceModel
) {
    actual fun analyze(
        poseResult: PoseResult,
        onAnalysisComplete: (PoseClassificationResult) -> Unit
    ) {
        model.reshape(34, 1)

        val start = Clock.System.now()
        val prediction = model.predict(
            inputData = poseResult.points.map { listOf(it.x, it.y) }.flatten().toFloatArray()
        )
        val end = Clock.System.now()

        onAnalysisComplete(
            PoseClassificationResult.WithResult((prediction == 1), end - start)
        )
    }

    actual fun close() {
        model.close()
    }

    actual class Builder {

        actual fun build( model: PoseClassificationModel): PoseClassifier {
            return PoseClassifier(
                TensorFlowInferenceModel.load(
                    File(model.savedModelPathName)
                )
            )
        }

    }

}