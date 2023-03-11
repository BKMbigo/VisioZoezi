package com.github.bkmbigo.visiozoezi.common.ml.pose

import com.github.bkmbigo.visiozoezi.common.ml.pose.models.AnalysisResult
import com.github.bkmbigo.visiozoezi.common.presentation.components.camera.CameraImage

actual class ImageAnalyzer {
    actual fun analyze(
        image: CameraImage,
        uiUpdateCallback: (AnalysisResult) -> Unit,
        isImageFlipped: Boolean
    ) {
    }

    actual fun close() {
    }

    actual companion object {
        actual class Builder {
            actual fun build(): ImageAnalyzer {
                TODO("Not yet implemented")
            }

        }
    }

}