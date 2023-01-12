package com.github.bkmbigo.visiozoezi.common.ml.pose.models

data class PosePoint(
    val landmark: PoseLandmark,
    val x: Float,
    val y: Float,
    val confidence: Float,
)