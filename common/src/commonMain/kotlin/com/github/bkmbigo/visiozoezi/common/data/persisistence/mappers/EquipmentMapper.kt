package com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers

import com.github.bkmbigo.visiozoezi.common.domain.models.Equipment
import com.github.bkmbigo.visiozoezi.common.data.persistence.Equipment as EquipmentDb

fun EquipmentDb.toEquipment(): Equipment {
    return Equipment(id, name)
}

fun Equipment.toEquipmentDb(): EquipmentDb {
    return EquipmentDb(id, name)
}

