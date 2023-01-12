package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun CameraCounter(
    counterState: MutableState<Int>,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        Text(
            text = "Counter: ${counterState.value}",
            fontSize = 19.sp,
            maxLines = 1,
            modifier = Modifier.align(Alignment.Center)
        )
        Button(
            onClick = {
                counterState.value = 0
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(text = "Reset Counter")
        }
    }
}