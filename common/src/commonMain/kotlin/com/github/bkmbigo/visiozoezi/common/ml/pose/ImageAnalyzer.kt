package com.github.bkmbigo.visiozoezi.common.ml.pose

import com.github.bkmbigo.visiozoezi.common.ml.pose.models.AnalysisResult
import com.github.bkmbigo.visiozoezi.common.presentation.components.camera.CameraImage

expect class ImageAnalyzer {
    fun analyze(
        image: CameraImage,
        uiUpdateCallback: (AnalysisResult) -> Unit,
        isImageFlipped: Boolean = false
    )

    fun close()

    companion object {
        class Builder{

            fun build(): ImageAnalyzer
        }
    }
}