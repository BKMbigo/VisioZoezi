package com.github.bkmbigo.visiozoezi.common.ml.classifier

import com.github.bkmbigo.visiozoezi.common.ml.classifier.models.PoseClassificationModel
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseResult

actual class PoseClassifier {
    actual fun analyze(
        poseResult: PoseResult,
        onAnalysisComplete: (PoseClassificationResult) -> Unit
    ) {
    }

    actual fun close() {
    }

    actual class Builder {
        actual fun build(model: PoseClassificationModel): PoseClassifier {
            TODO("Not yet implemented")
        }

    }

}