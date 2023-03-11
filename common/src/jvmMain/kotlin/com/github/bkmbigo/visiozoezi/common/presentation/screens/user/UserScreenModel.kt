package com.github.bkmbigo.visiozoezi.common.presentation.screens.user

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.bkmbigo.visiozoezi.common.domain.models.ExerciseStat
import com.github.bkmbigo.visiozoezi.common.domain.repositories.StatsRepository
import com.github.bkmbigo.visiozoezi.common.domain.repositories.utils.associateByDate
import com.github.bkmbigo.visiozoezi.common.domain.repositories.utils.getMonthlyStats
import com.github.bkmbigo.visiozoezi.common.domain.repositories.utils.getWeeklyStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class UserScreenModel(
    private val statsRepository: StatsRepository,
    private val coroutineScope: CoroutineScope  // Passed due to errors encountered while initiating voyager's scope
) : ScreenModel {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _currentWeeklyStats =
        MutableStateFlow<Map<LocalDate, List<ExerciseStat>>>(emptyMap())
    val currentWeeklyStats = _currentWeeklyStats.asStateFlow()

    private val _currentMonthlyStats =
        MutableStateFlow<Map<LocalDate, List<ExerciseStat>>>(emptyMap())
    val currentMonthlyStats = _currentMonthlyStats.asStateFlow()

    init {
        observeStats()
    }

    private fun observeStats() {
        coroutineScope.launch {
            _loading.value = true
            statsRepository.getAllStats().collect { statList ->
                _currentWeeklyStats.value = statList.getWeeklyStats().associateByDate()
                _currentMonthlyStats.value = statList.getMonthlyStats().associateByDate()
                _loading.value = false
            }
        }
    }
}