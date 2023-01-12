package com.github.bkmbigo.visiozoezi.common.ml.pose.models

data class PoseResult(
    val points: List<PosePoint>,
    val lines: List<PoseLine> = PoseEdges.getPoseLines(points)
)
