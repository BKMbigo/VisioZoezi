package com.github.bkmbigo.visiozoezi.common.presentation.components.image

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
actual fun ExerciseImage(gifUrl: String, modifier: Modifier) {
    GlideImage(
        model = gifUrl,
        contentDescription = null,
        modifier = modifier
    )
}