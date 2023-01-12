package com.github.bkmbigo.visiozoezi.common.ml.pose

import ai.djl.modality.cv.output.Joints
import ai.djl.modality.cv.output.Joints.Joint
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseLandmark
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PosePoint
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseResult

fun Joints.toPoseResult(): PoseResult {
    return PoseResult(
        points = joints.toPosePoints()
    )
}

fun List<Joint>.toPosePoints(): List<PosePoint> {
    return this.mapIndexed { index, joint ->
        PosePoint(
            PoseLandmark.getLandmark(index),
            joint.x.toFloat(),
            joint.y.toFloat(),
            joint.confidence.toFloat()
        )
    }
}