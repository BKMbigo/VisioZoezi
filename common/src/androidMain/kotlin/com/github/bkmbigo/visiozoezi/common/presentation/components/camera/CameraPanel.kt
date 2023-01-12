package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

import android.content.Context
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.UseCase
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.SwitchCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.github.bkmbigo.visiozoezi.common.ml.classifier.PoseClassificationResult
import com.github.bkmbigo.visiozoezi.common.ml.classifier.PoseClassifier
import com.github.bkmbigo.visiozoezi.common.ml.classifier.models.PoseClassificationModel
import com.github.bkmbigo.visiozoezi.common.ml.pose.ImageAnalyzer
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.AnalysisResult
import com.github.bkmbigo.visiozoezi.common.presentation.components.camera.utils.executor
import com.github.bkmbigo.visiozoezi.common.presentation.components.camera.utils.getCameraProvider
import com.github.bkmbigo.visiozoezi.common.presentation.components.results.DetectorView
import com.github.bkmbigo.visiozoezi.common.presentation.components.results.getPreviewImageBounds
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
actual fun CameraPanel(
    classificationResultsState: MutableState<PoseClassificationResult>,
    modifier: Modifier
) {
    val lifeCycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // State Variables
    val cameraPermissionState =
        rememberPermissionState(permission = android.Manifest.permission.CAMERA)

    val cameraSelector = remember { mutableStateOf(CameraSelector.DEFAULT_FRONT_CAMERA) }
    val camera = remember { mutableStateOf<Camera?>(null) }
    val flashLightState = remember { mutableStateOf(false) }
    val toggleFlashlightState = remember { mutableStateOf(false) }

    val imageAnalyzer =
        remember { ImageAnalyzer.Companion.Builder(context).build() }
    val poseClassifier =
        remember { PoseClassifier.Builder(context).build(PoseClassificationModel.PUSH_UP_POSE_CLASSIFICATION_MODEL) }

    val detectionResults = remember { mutableStateOf<AnalysisResult>(AnalysisResult.NoResult) }

    LaunchedEffect(detectionResults.value){
        withContext(Dispatchers.IO){
            if(detectionResults.value is AnalysisResult.WithResult) {
                poseClassifier.analyze(
                    (detectionResults.value as AnalysisResult.WithResult).poseResult,
                    onAnalysisComplete = { classificationResult ->
                        classificationResultsState.value = classificationResult
                    }
                )
            }
        }
    }

    CheckCameraPermission(cameraPermissionState) {


        BoxWithConstraints(modifier = modifier) {

            CameraPreview(
                cameraSelector = cameraSelector.value,
                imageAnalyzer = imageAnalyzer,
                lifecycleOwner = lifeCycleOwner,
                onResultsReady = { analysisResult ->
                    detectionResults.value = analysisResult
                },
                onCameraOpened = { cam ->
                    camera.value = cam
                    flashLightState.value = cam.cameraInfo.hasFlashUnit()
                },
                modifier = Modifier.fillMaxSize(),
            )


            DetectionPanel(
                resultsState = detectionResults,
                cameraSelector = cameraSelector,
                constraints = constraints,
                modifier = Modifier.fillMaxSize()
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
                    .align(Alignment.BottomCenter),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledIconButton(
                    onClick = {
                        cameraSelector.value =
                            if (cameraSelector.value == CameraSelector.DEFAULT_BACK_CAMERA)
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            else
                                CameraSelector.DEFAULT_BACK_CAMERA
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.SwitchCamera,
                        contentDescription = null
                    )
                }
                FilledIconButton(
                    onClick = {
                        camera.value?.cameraControl?.enableTorch(!toggleFlashlightState.value)
                        toggleFlashlightState.value = !toggleFlashlightState.value
                    },
                    enabled = flashLightState.value
                ) {
                    Icon(
                        imageVector = if (toggleFlashlightState.value) Icons.Filled.FlashlightOff
                        else Icons.Filled.FlashlightOn,
                        contentDescription = null
                    )
                }
            }

        }
    }
}

@Composable
fun CameraPreview(
    cameraSelector: CameraSelector,
    imageAnalyzer: ImageAnalyzer,
    lifecycleOwner: LifecycleOwner,
    onResultsReady: (AnalysisResult) -> Unit,
    onCameraOpened: (Camera) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    AndroidView(
        modifier = modifier.fillMaxSize(),
        update = { previewView ->
            val previewUseCase = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }

            val analysisUseCase = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                .build()
                .apply {
                    setAnalyzer(context.executor) { image ->
                        imageAnalyzer.analyze(
                            image = CameraImage.Companion.Builder(image).build(),
                            uiUpdateCallback = { analysisResult ->
                                onResultsReady(analysisResult)
                                image.close()
                            },
                            isImageFlipped = cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA
                        )
                    }
                }

            coroutineScope.launch {
                suspend fun setUp(
                    context: Context,
                    lifecycleOwner: LifecycleOwner,
                    cameraSelector: CameraSelector,
                    previewUseCase: UseCase,
                    analysisUseCase: UseCase
                ): Camera {
                    val cameraProvider = context.getCameraProvider()
                    try {
                        cameraProvider.unbindAll()
                        return cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, previewUseCase, analysisUseCase
                        )
                    } catch (e: Exception) {
                        throw e
                    }
                }

                setUp(
                    context, lifecycleOwner, cameraSelector, previewUseCase, analysisUseCase
                ).also { cam ->
                    onCameraOpened(cam)
                }
            }
        },
        factory = { previewContext ->
            val previewView = PreviewView(previewContext).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            previewView
        })

}

@Composable
fun DetectionPanel(
    resultsState: State<AnalysisResult>,
    cameraSelector: State<CameraSelector>,
    constraints: Constraints,
    modifier: Modifier
) {
    if (resultsState.value is AnalysisResult.WithResult) {
        DetectorView(
            pose = (resultsState.value as AnalysisResult.WithResult).poseResult,
            bounds = getPreviewImageBounds(
                sourceImageWidth = (resultsState.value as AnalysisResult.WithResult).imageMetadata.width,
                sourceImageHeight = (resultsState.value as AnalysisResult.WithResult).imageMetadata.height,
                viewWidth = constraints.maxWidth,
                viewHeight = constraints.maxHeight

            ),
            isImageFlipped = cameraSelector.value == CameraSelector.DEFAULT_FRONT_CAMERA,
            modifier = modifier

        )
    }


}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CheckCameraPermission(
    permissionState: PermissionState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    if (!permissionState.status.isGranted) {
        RequestPermission(permissionState, modifier)
    } else {
        content()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermission(
    permissionState: PermissionState,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "The application requires the Camera permission in order to show the Camera",
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = { permissionState.launchPermissionRequest() }
            ) {
                Image(
                    imageVector = Icons.Filled.Camera,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
                Text(
                    text = "Grant Permission"
                )
            }
        }
    }
}