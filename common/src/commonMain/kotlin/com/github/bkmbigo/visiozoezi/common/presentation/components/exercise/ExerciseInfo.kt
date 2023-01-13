package com.github.bkmbigo.visiozoezi.common.presentation.components.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullExerciseInfo(
    exercise: Exercise,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier) {
        if (constraints.maxHeight > 200.dp.value) {
            Column(
                modifier = Modifier.fillMaxSize().padding(4.dp)
            ) {
                ExerciseName(
                    name = exercise.name,
                    modifier = Modifier.align(Alignment.CenterHorizontally).weight(0.4f, true),
                )
                Spacer(Modifier.height(4.dp))
                Divider(Modifier.padding(horizontal = 4.dp))
                Spacer(Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().weight(0.6f),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ExerciseInfoCard(
                        "Equipment:",
                        propertyValue = exercise.equipment.name,
                        Modifier.padding(horizontal = 4.dp)
                    )

                    ExerciseInfoCard(
                        "Body Part:",
                        propertyValue = exercise.bodyPart.name,
                        Modifier.padding(horizontal = 4.dp)
                    )

                    ExerciseInfoCard(
                        "Target Muscle:",
                        propertyValue = exercise.targetMuscle.name,
                        Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().padding(4.dp)
            ) {
                ExerciseName(
                    name = exercise.name,
                    modifier = Modifier.align(Alignment.CenterHorizontally).weight(0.8f, true),
                )
            }
        }
    }
}

@Composable
fun VerticalExerciseInfo(
    exercise: Exercise,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        if (constraints.maxHeight > 300.dp.value) {
            Column {
                ExerciseName(exercise.name, Modifier.fillMaxWidth().padding(horizontal = 4.dp))
                Spacer(Modifier.height(4.dp))
                Divider(Modifier.padding(4.dp))
                Spacer(Modifier.height(4.dp))
                ExerciseInfoCard(
                    propertyName = "Equipment:",
                    propertyValue = exercise.equipment.name,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp)
                )
                ExerciseInfoCard(
                    propertyName = "Body Part:",
                    propertyValue = exercise.equipment.name,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp)
                )
                ExerciseInfoCard(
                    propertyName = "Target Muscle: ",
                    propertyValue = exercise.equipment.name,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(4.dp)
                )
            }
        } else {
            ExerciseName(
                exercise.name,
                Modifier.fillMaxWidth().padding(horizontal = 4.dp).align(Alignment.Center)
            )
        }
    }

}

@Composable
private fun ExerciseName(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = name,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.headlineSmall,
        modifier = modifier,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExerciseInfoCard(
    propertyName: String,
    propertyValue: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp
        ),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = propertyName,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = propertyValue,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}