package com.github.bkmbigo.visiozoezi.common.presentation.screens.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.github.bkmbigo.visiozoezi.common.domain.repositories.StatsRepository
import com.github.bkmbigo.visiozoezi.common.presentation.components.stats.ClearStatsDialog
import com.github.bkmbigo.visiozoezi.common.presentation.components.stats.MonthlyStats
import com.github.bkmbigo.visiozoezi.common.presentation.components.stats.WeeklyStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserScreen(
    private val statsRepository: StatsRepository
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val coroutineScope = rememberCoroutineScope()

        val screenModel = rememberScreenModel { UserScreenModel(statsRepository, coroutineScope) }

        val weeklyStats = screenModel.currentWeeklyStats.collectAsState()
        val monthlyStats = screenModel.currentMonthlyStats.collectAsState()

        // State Variables
        val showClearStatsDialogState = remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "Exercise") },
                    navigationIcon = {
                        IconButton(onClick = { navigator?.pop() }) {
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
            }
        ) {
            Box(modifier = Modifier.padding(top = it.calculateTopPadding()).padding(4.dp)) {
                if (showClearStatsDialogState.value) {
                    ClearStatsDialog(
                        dialogVisibilityState = showClearStatsDialogState,
                        onConfirm = {
                            coroutineScope.launch(Dispatchers.IO) {
                                statsRepository.deleteAllStats()
                            }
                        }
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
}