package com.github.bkmbigo.visiozoezi.common.presentation.components.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun ExerciseImage(
    gifUrl: String,
    modifier: Modifier = Modifier,
)