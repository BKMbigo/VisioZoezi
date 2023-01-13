package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.bkmbigo.visiozoezi.common.ml.classifier.PoseClassificationResult
import com.github.bkmbigo.visiozoezi.common.ml.pose.ImageAnalyzer
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.AnalysisResult
import com.github.sarxos.webcam.Webcam
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
actual fun CameraPanel(
    classificationResultsState: MutableState<PoseClassificationResult>,
    modifier: Modifier
) {
    val cameras = remember { mutableStateOf(Webcam.getWebcams()) }
    val camera = remember { mutableStateOf<Webcam?>(null) }

    val imageAnalyzer = remember { mutableStateOf<ImageAnalyzer?>(null) }
    //val poseClassifier = remember { mutableStateOf<PoseClassifier?>(null) }

    val detectionResultState = remember { mutableStateOf<AnalysisResult>(AnalysisResult.NoResult) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            imageAnalyzer.value = ImageAnalyzer.Companion.Builder().build()
        }
    }

//    LaunchedEffect(Unit) {
//        withContext(Dispatchers.IO) {
//            poseClassifier.value = PoseClassifier.Builder()
//                .build(PoseClassificationModel.PUSH_UP_POSE_CLASSIFICATION_MODEL)
//        }
//    }
//
//    LaunchedEffect(detectionResultState.value) {
//        if (detectionResultState.value is AnalysisResult.WithResult) {
//            if (poseClassifier.value != null) {
//                withContext(Dispatchers.IO) {
//                    poseClassifier.value!!.analyze(
//                        poseResult = (detectionResultState.value as AnalysisResult.WithResult).poseResult,
//                        onAnalysisComplete = { classificationResult ->
//                            if (classificationResult is PoseClassificationResult.WithResult) {
//                                classificationResultsState.value =
//                                    PoseClassificationResult.WithResult(
//                                        classificationResult.data,
//                                        classificationResult.processTime
//                                    )
//                            }
//                        }
//
//                    )
//                }
//            }
//        }
//    }

    LaunchedEffect(camera.value, imageAnalyzer) {
        if (camera.value != null) {
            while (camera.value!!.isOpen) {
                val image = camera.value!!.image
                if (imageAnalyzer.value != null) {
                    imageAnalyzer.value!!.analyze(
                        image = CameraImage.Companion.Builder(image).build(),
                        uiUpdateCallback = { analysisResult ->
                            detectionResultState.value = analysisResult
                        }, true
                    )
                }
            }
        }
    }

    BoxWithConstraints(modifier = modifier) {
        if (camera.value == null) {
            if (cameras.value.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.Camera,
                        contentDescription = null
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "No cameras found on your system. Please attach a camera to continue",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                CameraChooser(
                    cameras, camera, Modifier.align(Alignment.Center)
                )
            }
        } else {
            CameraPreview(camera, Modifier.fillMaxSize())
        }
    }

}