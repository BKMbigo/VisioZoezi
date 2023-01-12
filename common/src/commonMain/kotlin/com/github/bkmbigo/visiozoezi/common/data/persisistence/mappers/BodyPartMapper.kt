package com.github.bkmbigo.visiozoezi.common.data.persisistence.mappers

import com.github.bkmbigo.visiozoezi.common.domain.models.BodyPart
import com.github.bkmbigo.visiozoezi.common.data.persistence.Body_part as BodyPartDb

fun BodyPartDb.toBodyPart(): BodyPart {
    return BodyPart(id, name)
}

fun BodyPart.toBodyPartDb() : BodyPartDb {
    return BodyPartDb(id, name)
}