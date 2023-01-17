package com.github.bkmbigo.visiozoezi.common.presentation.components.stats

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

@Composable
actual fun ClearStatsDialog(
    dialogVisibilityState: MutableState<Boolean>,
    onConfirm: () -> Unit,
    modifier: Modifier
) {
    AlertDialog(
        onDismissRequest = { dialogVisibilityState.value = false },
        confirmButton = {
            Button(onClick = { onConfirm(); dialogVisibilityState.value = false }) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { dialogVisibilityState.value = false }) {
                Text(text = "Discard")
            }
        },
        title = { Text(text = "Clear Exercise Stats") },
        text = { Text(text = "Do you want to proceed and delete all recorded stats?") },
        containerColor = MaterialTheme.colorScheme.surface,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    )
}