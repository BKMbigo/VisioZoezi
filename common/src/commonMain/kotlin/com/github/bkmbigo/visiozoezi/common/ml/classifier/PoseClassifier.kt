package com.github.bkmbigo.visiozoezi.common.ml.classifier

import com.github.bkmbigo.visiozoezi.common.ml.classifier.models.PoseClassificationModel
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseResult

expect class PoseClassifier{

    fun analyze(
        poseResult: PoseResult,
        onAnalysisComplete: (PoseClassificationResult) -> Unit
    )

    fun close()

    class Builder{

        fun build(model: PoseClassificationModel) : PoseClassifier
    }

}