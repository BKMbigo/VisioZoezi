package com.github.bkmbigo.visiozoezi.common.presentation.screens.home

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.repositories.ExerciseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val exerciseRepository: ExerciseRepository,
    private val coroutineScope: CoroutineScope  //Errors while initiating screen model's coroutine scope
): ScreenModel {

    val exerciseList = MutableStateFlow<List<Exercise>>(emptyList())

    init {
        coroutineScope.launch(Dispatchers.IO) {
            exerciseRepository.getAllExercises().collect { list ->
                exerciseList.value = list
            }
        }
    }


}