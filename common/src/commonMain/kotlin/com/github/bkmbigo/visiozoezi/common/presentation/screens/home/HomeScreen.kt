package com.github.bkmbigo.visiozoezi.common.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.repositories.ExerciseRepository
import com.github.bkmbigo.visiozoezi.common.domain.repositories.StatsRepository
import com.github.bkmbigo.visiozoezi.common.presentation.components.exercise.VerticalExerciseList
import com.github.bkmbigo.visiozoezi.common.presentation.screens.exercise.ExerciseScreen
import com.github.bkmbigo.visiozoezi.common.presentation.screens.user.UserScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeScreen(
    private val exerciseRepository: ExerciseRepository,
    private val statsRepository: StatsRepository
) : Screen {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val coroutineScope = rememberCoroutineScope()
        val screenModel =
            rememberScreenModel { HomeScreenModel(exerciseRepository, coroutineScope) }

        var job: Job? = null

        //State Variables
        val exerciseList = remember { mutableStateOf<List<Exercise>>(emptyList()) }

        val loading = remember { mutableStateOf(false) }
        val awaitingUserInput = remember { mutableStateOf(false) }

        val searchInputState = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }

        LaunchedEffect(searchInputState.value) {
            awaitingUserInput.value = true
            job?.cancel()
            delay(5000)
            awaitingUserInput.value = false
            job = coroutineScope.launch {
                loading.value = true
                if (searchInputState.value.text.isBlank()) {
                    loading.value = false
                    screenModel.exerciseList.collect { list ->
                        exerciseList.value = list
                    }
                } else {
//                    exerciseList.value =
//                        exerciseRepository.searchExercise(searchInputState.value.text)
                    loading.value = false
                }

            }

        }

        Scaffold { padding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(top = padding.calculateTopPadding() + 6.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    if (loading.value) LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        trackColor = MaterialTheme.colorScheme.background
                    )
                }
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)) {
                    OutlinedTextField(
                        value = searchInputState.value,
                        onValueChange = { searchInputState.value = it },
                        modifier = Modifier.weight(1f, true).padding(4.dp).padding(horizontal = 4.dp),
                        leadingIcon = {
                            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    searchInputState.value = TextFieldValue("")
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Cancel,
                                    contentDescription = null
                                )
                            }
                        },
                        label = {
                            Text(text = "Search...")
                        }
                    )
                    FilledIconButton(
                        onClick = { navigator?.push(UserScreen()) },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PersonOutline,
                            contentDescription = null
                        )
                    }
                }

                VerticalExerciseList(
                    exerciseListState = exerciseList,
                    loading = loading,
                    onItemSelected = { navigator?.push(ExerciseScreen(it, statsRepository, true)) },
                    modifier = Modifier.weight(1f, fill = true)
                )
            }
        }
    }


}