package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.sarxos.webcam.Webcam

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraChooser(
    cameras: State<List<Webcam>>,
    camera: MutableState<Webcam?>,
    modifier: Modifier,
) {
    Column(modifier) {
        Text(
            text = "Please Select a camera to continue",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(4.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(cameras.value) { webcam ->
                Card(
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(6.dp),
                    modifier = Modifier.padding(4.dp).clickable { camera.value = webcam }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Camera,
                        contentDescription = null
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = webcam.name,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}