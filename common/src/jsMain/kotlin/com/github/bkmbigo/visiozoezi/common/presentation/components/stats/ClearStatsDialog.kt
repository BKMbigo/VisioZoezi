package com.github.bkmbigo.visiozoezi.common.presentation.components.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
actual fun ClearStatsDialog(
    dialogVisibilityState: MutableState<Boolean>,
    onConfirm: () -> Unit,
    modifier: Modifier
) {
}