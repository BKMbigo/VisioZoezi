package com.github.bkmbigo.visiozoezi.common.ml.pose.models

sealed class AnalysisResult {
    object NoResult : AnalysisResult()
    class Empty(processTime: Long) : AnalysisResult()
    data class WithResult(
        val poseResult: PoseResult,
        val processTime: Long,
        val imageMetadata: ImageMetadata
    ) : AnalysisResult()
}