package com.github.bkmbigo.visiozoezi.common.presentation.components.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.bkmbigo.visiozoezi.common.domain.models.ExerciseStat
import com.github.bkmbigo.visiozoezi.common.domain.repositories.utils.getDuration
import kotlinx.datetime.LocalDate
import kotlin.time.Duration


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyStats(
    statsState: State<Map<LocalDate, List<ExerciseStat>>>,
    modifier: Modifier = Modifier
) {

    val totalDurationState = remember { mutableStateOf(Duration.ZERO) }

    LaunchedEffect(statsState.value) {
        totalDurationState.value = statsState.value.values.flatten().getDuration()
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Weekly Stats",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Exercise Time: ${totalDurationState.value.toString()}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(4.dp))
            if (statsState.value.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(4.dp),
                ) {
                    items(statsState.value.keys.toList()) { date ->
                        RowStat(
                            title = date.dayOfWeek.name,
                            duration = statsState.value[date]!!.getDuration(),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyStats(
    statsState: State<Map<LocalDate, List<ExerciseStat>>>,
    modifier: Modifier = Modifier,
) {
    val totalDurationState = remember { mutableStateOf(Duration.ZERO) }
    val sortedMapState = remember { mutableStateOf<Map<LocalDate, List<ExerciseStat>>>(emptyMap()) }

    LaunchedEffect(statsState.value) {
        totalDurationState.value = statsState.value.values.flatten().getDuration()
        sortedMapState.value = statsState.value.toList().sortedByDescending { (_, statList) ->
            statList.getDuration()
        }.toMap()
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Monthly Stats",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Exercise Time: ${totalDurationState.value.toString()}",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.height(4.dp))
            if (sortedMapState.value.values.isNotEmpty()) {
                val maximumEntries = minOf(sortedMapState.value.size, 5)
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(4.dp),
                ) {
                    items(sortedMapState.value.keys.toList().subList(0, maximumEntries)) { date ->
                        RowStat(
                            title = date.toString(),
                            duration = sortedMapState.value[date]!!.getDuration(),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowStat(
    title: String,
    duration: Duration,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = "duration.toString()"
        )
    }
}