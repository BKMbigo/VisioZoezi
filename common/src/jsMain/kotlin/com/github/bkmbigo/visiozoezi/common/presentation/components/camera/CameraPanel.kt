package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.github.bkmbigo.visiozoezi.common.ml.classifier.PoseClassificationResult

@Composable
actual fun CameraPanel(
    classificationResultsState: MutableState<PoseClassificationResult>,
    modifier: Modifier
) {
}