package com.github.bkmbigo.visiozoezi.common.presentation.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.bkmbigo.visiozoezi.common.domain.models.ExerciseStat
import com.github.bkmbigo.visiozoezi.common.presentation.components.stats.ClearStatsDialog
import com.github.bkmbigo.visiozoezi.common.presentation.components.stats.MonthlyStats
import com.github.bkmbigo.visiozoezi.common.presentation.components.stats.WeeklyStats
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserScreen(
    weeklyStats: State<Map<LocalDate, List<ExerciseStat>>>,
    monthlyStats: State<Map<LocalDate, List<ExerciseStat>>>,
    modifier: Modifier = Modifier,
    clearStats: () -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    // State Variables
    val showClearStatsDialogState = remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Exercise") },
                navigationIcon = {
                    IconButton(onClick = { navigateBack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showClearStatsDialogState.value = true }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DeleteForever,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) {
        Box(modifier = Modifier.padding(top = it.calculateTopPadding()).padding(4.dp)) {
            if (showClearStatsDialogState.value) {
                ClearStatsDialog(
                    dialogVisibilityState = showClearStatsDialogState,
                    onConfirm = { clearStats() }
                )
            }
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                WeeklyStats(
                    statsState = weeklyStats,
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                )
                Spacer(Modifier.height(4.dp))
                MonthlyStats(
                    statsState = monthlyStats,
                    modifier = Modifier.fillMaxWidth().padding(4.dp)
                )
            }
        }
    }
}