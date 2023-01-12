package com.github.bkmbigo.visiozoezi.common.ml.classifier

import kotlin.time.Duration

sealed class PoseClassificationResult {
    object NoResult: PoseClassificationResult()
    data class Empty(val processTime: Duration): PoseClassificationResult()
    data class WithResult(
        val data: Boolean,  // Binary Classifier is used in the project
        val processTime: Duration
    ): PoseClassificationResult()
}