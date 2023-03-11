package com.github.bkmbigo.visiozoezi.common.presentation.screens.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.domain.models.Exercise
import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle
import com.github.bkmbigo.visiozoezi.common.domain.repositories.ExerciseRepository
import com.github.bkmbigo.visiozoezi.common.domain.repositories.StatsRepository
import com.github.bkmbigo.visiozoezi.common.presentation.components.exercise.VerticalExerciseList
import com.github.bkmbigo.visiozoezi.common.presentation.screens.exercise.ExerciseScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class FilterScreen(
    private val exerciseRepository: ExerciseRepository,
    private val statsRepository: StatsRepository
) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        var job: Job? = null

        // State Variables
        val loading = remember { mutableStateOf(false) }
        val awaitingUserInput = remember { mutableStateOf(false) }

        val exerciseList = remember { mutableStateOf<List<Exercise>>(emptyList()) }
        val searchInputState = rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(TextFieldValue(""))
        }
        val currentFilterState = remember { mutableStateOf(FilterState.EQUIPMENT) }
        val equipmentFilterState = remember { mutableStateOf<Equipment?>(null) }
        val bodyPartFilterState = remember { mutableStateOf<BodyPart?>(null) }
        val targetMuscleFilterState = remember { mutableStateOf<TargetMuscle?>(null) }

        val equipmentList = remember { mutableStateOf<List<Equipment>>(emptyList()) }
        val bodyPartList = remember { mutableStateOf<List<BodyPart>>(emptyList()) }
        val targetMuscleList = remember { mutableStateOf<List<TargetMuscle>>(emptyList()) }

        LaunchedEffect(
            searchInputState.value,
            equipmentFilterState.value,
            bodyPartFilterState.value,
            targetMuscleFilterState.value
        ) {
            awaitingUserInput.value = true
            job?.cancel()
            job = launch(Dispatchers.IO) {
                delay(4000)
                awaitingUserInput.value = false
                loading.value = true
                if (isActive) {
                    exerciseList.value =
                        exerciseRepository.searchExercise(
                            query = searchInputState.value.text,
                            equipment = equipmentFilterState.value,
                            bodyPart = bodyPartFilterState.value,
                            targetMuscle = targetMuscleFilterState.value
                        )
                    loading.value = false
                }
            }
        }

        LaunchedEffect(Unit) {
            launch(Dispatchers.IO) {
                exerciseRepository.getAllEquipment().collect {
                    equipmentList.value = it
                }
            }
            launch(Dispatchers.IO) {
                exerciseRepository.getAllBodyParts().collect {
                    bodyPartList.value = it
                }
            }
            launch(Dispatchers.IO) {
                exerciseRepository.getAllTargetMuscle().collect {
                    targetMuscleList.value = it
                }
            }
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Filter Screen"
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { navigator?.pop() }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding() + 2.dp)
                    .fillMaxSize()
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(2.dp).padding(horizontal = 4.dp)) {
                    OutlinedTextField(
                        value = searchInputState.value,
                        onValueChange = { state -> searchInputState.value = state },
                        modifier = Modifier.weight(1f, true).padding(4.dp)
                            .padding(horizontal = 4.dp),
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
                    Button(
                        onClick = {
                            searchInputState.value = TextFieldValue("")
                            equipmentFilterState.value = null
                            bodyPartFilterState.value = null
                            targetMuscleFilterState.value = null
                        }
                    ) {
                        Text(text = "Reset")
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (currentFilterState.value == FilterState.EQUIPMENT) {
                            Button(
                                onClick = { equipmentFilterState.value = null }
                            ) {
                                Text(text = "Equipment")
                            }
                        } else {
                            OutlinedButton(
                                onClick = { currentFilterState.value = FilterState.EQUIPMENT }
                            ) {
                                Text(text = "Equipment")
                            }
                        }
                        if (currentFilterState.value == FilterState.BODY_PART) {
                            Button(
                                onClick = { bodyPartFilterState.value = null }
                            ) {
                                Text(text = "Body Part")
                            }
                        } else {
                            OutlinedButton(
                                onClick = { currentFilterState.value = FilterState.BODY_PART }
                            ) {
                                Text(text = "Body Part")
                            }
                        }
                        if (currentFilterState.value == FilterState.TARGET_MUSCLE) {
                            Button(
                                onClick = { targetMuscleFilterState.value = null }
                            ) {
                                Text(text = "Target Muscle")
                            }
                        } else {
                            OutlinedButton(
                                onClick = { currentFilterState.value = FilterState.TARGET_MUSCLE }
                            ) {
                                Text(text = "Target Muscle")
                            }
                        }
                    }
                    Spacer(Modifier.height(2.dp))
                    Divider(Modifier.padding(4.dp))
                    Spacer(Modifier.height(2.dp))
                    when (currentFilterState.value) {
                        FilterState.EQUIPMENT -> {
                            EquipmentFilterRow(
                                equipmentList,
                                equipmentFilterState,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        FilterState.BODY_PART -> {
                            BodyPartFilterRow(
                                bodyPartList,
                                bodyPartFilterState,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        FilterState.TARGET_MUSCLE -> {
                            TargetMuscleFilterRow(
                                targetMuscleList,
                                targetMuscleFilterState,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
                VerticalExerciseList(
                    exerciseList,
                    loading,
                    onItemSelected = { navigator?.push(ExerciseScreen(it, statsRepository)) },
                    modifier = Modifier.weight(0.5f).fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EquipmentFilterRow(
    equipmentList: State<List<Equipment>>,
    equipmentFilterState: MutableState<Equipment?>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    ) {
        items(equipmentList.value) { equipment ->
            FilterChip(
                modifier = Modifier.padding(horizontal = 4.dp),
                selected = equipment == equipmentFilterState.value,
                onClick = {
                    if (equipmentFilterState.value != equipment) {
                        equipmentFilterState.value = equipment
                    } else {
                        equipmentFilterState.value = null
                    }
                },
                label = { Text(text = equipment.name) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BodyPartFilterRow(
    bodyPartList: State<List<BodyPart>>,
    bodyPartEquipmentList: MutableState<BodyPart?>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    ) {
        items(bodyPartList.value) { bodyPart ->
            FilterChip(
                modifier = Modifier.padding(horizontal = 4.dp),
                selected = bodyPart == bodyPartEquipmentList.value,
                onClick = {
                    if (bodyPartEquipmentList.value != bodyPart) {
                        bodyPartEquipmentList.value = bodyPart
                    } else {
                        bodyPartEquipmentList.value = null
                    }

                },
                label = { Text(text = bodyPart.name) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TargetMuscleFilterRow(
    targetMuscleList: State<List<TargetMuscle>>,
    targetMuscleFilterState: MutableState<TargetMuscle?>,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp)
    ) {
        items(targetMuscleList.value) { targetMuscle ->
            FilterChip(
                modifier = Modifier.padding(horizontal = 4.dp),
                selected = targetMuscle == targetMuscleFilterState.value,
                onClick = {
                    if (targetMuscleFilterState.value != targetMuscle) {
                        targetMuscleFilterState.value = targetMuscle
                    } else {
                        targetMuscleFilterState.value = null
                    }
                },
                label = { Text(text = targetMuscle.name) },
            )
        }
    }
}

private enum class FilterState {
    EQUIPMENT,
    BODY_PART,
    TARGET_MUSCLE
}