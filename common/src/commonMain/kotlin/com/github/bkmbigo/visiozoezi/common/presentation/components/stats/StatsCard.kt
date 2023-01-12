package com.github.bkmbigo.visiozoezi.common.presentation.components.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.bkmbigo.visiozoezi.common.domain.models.ExerciseStat
import com.github.bkmbigo.visiozoezi.common.domain.repositories.utils.getDuration
import kotlin.time.DurationUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatsCard(
    stats: State<List<ExerciseStat>>,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(Modifier.padding(6.dp)) {
            Text(
                text = "Weekly Stats",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(6.dp))

        }
    }
}

@Composable
fun WeeklyStats(
    data: State<List<ExerciseStat>>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(horizontal = 6.dp)) {
        val duration = data.value.getDuration()
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Hours Exercised:")
            Text(text = "${duration.toString(DurationUnit.HOURS, 0)} Hours")
        }
    }

}