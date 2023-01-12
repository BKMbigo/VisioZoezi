package com.github.bkmbigo.visiozoezi.common.ml.pose

import android.content.Context
import android.os.SystemClock
import androidx.camera.core.ImageProxy
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.AnalysisResult
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.ImageMetadata
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseLandmark
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PosePoint
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseResult
import com.github.bkmbigo.visiozoezi.common.presentation.components.camera.CameraImage
import org.jetbrains.kotlinx.dl.onnx.inference.ONNXModelHub
import org.jetbrains.kotlinx.dl.onnx.inference.ONNXModels
import org.jetbrains.kotlinx.dl.onnx.inference.executionproviders.ExecutionProvider
import org.jetbrains.kotlinx.dl.onnx.inference.inferUsing
import org.jetbrains.kotlinx.dl.onnx.inference.posedetection.SinglePoseDetectionModel
import org.jetbrains.kotlinx.dl.onnx.inference.posedetection.detectPose

actual class ImageAnalyzer(
    private val model: SinglePoseDetectionModel
) {
    actual fun analyze(
        image: CameraImage,
        uiUpdateCallback: (AnalysisResult) -> Unit,
        isImageFlipped: Boolean
    ) {

        val start = SystemClock.uptimeMillis()
        val result = model.inferUsing(ExecutionProvider.CPU()) {
            it.detectPose(image.image as ImageProxy)
        }
        val end = SystemClock.uptimeMillis()

        uiUpdateCallback(
            if (result.landmarks.isEmpty()) {
                AnalysisResult.Empty(processTime = end - start)
            } else {
                val maxConfidence = result.landmarks.maxOf { it.probability }

                if (maxConfidence > DEFAULT_THRESHOLD) {
                    AnalysisResult.WithResult(
                        poseResult = PoseResult(
                            points = result.landmarks.mapIndexed { index, landmark ->
                                PosePoint(
                                    landmark = PoseLandmark.getLandmark(index),
                                    x = landmark.x,
                                    y = landmark.y,
                                    confidence = landmark.probability
                                )
                            }
                        ),
                        processTime = end - start,
                        imageMetadata = ImageMetadata(
                            width = image.width,
                            height = image.height,
                            isImageFlipped = isImageFlipped,
                            rotationDegrees = (image.image as ImageProxy).imageInfo.rotationDegrees
                        )
                    )
                } else {
                    AnalysisResult.Empty(processTime = end - start)
                }
            }
        )

    }

    actual fun close() {
        model.close()
    }

    actual companion object {
        actual class Builder(context: Context) {
            private val hub: ONNXModelHub = ONNXModelHub(context)

            actual fun build(): ImageAnalyzer {
                return ImageAnalyzer(
                    ONNXModels.PoseDetection.MoveNetSinglePoseLighting.pretrainedModel(hub)
                )
            }
        }

        const val DEFAULT_THRESHOLD = 0.70f
    }

}