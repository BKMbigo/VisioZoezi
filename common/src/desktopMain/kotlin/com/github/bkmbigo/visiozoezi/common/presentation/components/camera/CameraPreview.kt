package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamPanel
import java.awt.BorderLayout
import javax.swing.JPanel

@Composable
fun CameraPreview(
    camera: State<Webcam?>,
    modifier: Modifier = Modifier
) {
    SwingPanel(
        modifier = modifier,
        factory = {
            JPanel().apply {
                this.layout = BorderLayout()
                add(WebcamPanel(camera.value!!))
            }
        }
    )
}