package com.github.bkmbigo.visiozoezi.common.presentation.components.image

import androidx.compose.foundation.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.animatedimage.animate
import org.jetbrains.compose.animatedimage.loadAnimatedImage
import org.jetbrains.compose.resources.LoadState
import org.jetbrains.compose.resources.load

@Composable
actual fun ExerciseImage(gifUrl: String, modifier: Modifier) {
    when (val animatedImage = load { loadAnimatedImage(gifUrl) }) {
        is LoadState.Success -> Image(
            bitmap = animatedImage.value.animate(),
            contentDescription = null,
            modifier = modifier
        )

        is LoadState.Loading -> CircularProgressIndicator(modifier = modifier)
        is LoadState.Error -> Text("Error!", modifier = modifier)
    }
}