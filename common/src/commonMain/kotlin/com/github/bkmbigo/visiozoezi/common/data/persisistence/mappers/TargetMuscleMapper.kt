package com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers

import com.github.bkmbigo.visiozoezi.common.domain.models.TargetMuscle
import com.github.bkmbigo.visiozoezi.common.data.persistence.Target_muscle as TargetMuscleDb

fun TargetMuscleDb.toTargetMuscle(): TargetMuscle {
    return TargetMuscle(id, name)
}

fun TargetMuscle.toTargetMuscleDb(): TargetMuscleDb {
    return TargetMuscleDb(id, name)
}