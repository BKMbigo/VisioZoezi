package com.github.bkmbigo.visiozoezi.common.presentation.components.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun ClearStatsDialog(
    dialogVisibilityState: MutableState<Boolean>,
    onConfirm: () -> Unit,
    modifier: Modifier
) {
    Dialog(
        onCloseRequest = { dialogVisibilityState.value = false },
        title = "Clear Exercise Stats",
    ) {
        Card(
            modifier = Modifier.padding(4.dp),
            elevation = CardDefaults.elevatedCardElevation(6.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Do you want to proceed and delete all recorded stats?",
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(
                        onClick = { dialogVisibilityState.value = false }
                    ) {
                        Text(text = "Discard")
                    }
                    Button(
                        onClick = {
                            onConfirm()
                            dialogVisibilityState.value = false
                        }
                    ) {
                        Text(text = "Reset")
                    }
                }
            }

        }
    }
}