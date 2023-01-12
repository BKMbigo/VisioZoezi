package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * The Function notifies the user that their Camera will be in Use
 */
@Composable
fun CameraNotice(
    onAccept: () -> Unit,
    onDeny: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "The Application needs to access to your webcam to enable pose detection and tracking.\n" +
                    "Frames obtained from the camera are used locally within the application and the camera will be closed after the activity is done."
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onAccept() }
            ) {
                Text(text = "Accept")
            }

            Button(
                onClick = { onDeny() }
            ) {
                Text(text = "Deny")
            }
        }
    }
}