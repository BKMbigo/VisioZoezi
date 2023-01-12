package com.github.bkmbigo.visiozoezi.common.presentation.components.exercise

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise

@Composable
fun VerticalExerciseList(
    exerciseListState: State<List<Exercise>>,
    loading: State<Boolean>,
    onItemSelected: (Exercise) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 200.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(6.dp),
            state = rememberLazyGridState(),
        ) {
            items(exerciseListState.value) { exercise ->
                ExerciseItem(
                    exercise = exercise,
                    onItemClicked = { onItemSelected(exercise) },
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                )
            }
        }
        if (loading.value)
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
    }
}