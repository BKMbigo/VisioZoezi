package com.github.bkmbigo.visiozoezi.common.ml.pose.models

data class ImageMetadata(
    val width: Int,
    val height: Int,
    val isImageFlipped: Boolean
) {
    constructor(width: Int, height: Int, isImageFlipped: Boolean, rotationDegrees: Int) : this(
        if (areDimensionsSwitched(rotationDegrees)) height else width,
        if (areDimensionsSwitched(rotationDegrees)) width else height,
        isImageFlipped
    )

    companion object {
        private fun areDimensionsSwitched(rotationDegrees: Int): Boolean {
            return rotationDegrees == 90 || rotationDegrees == 270
        }
    }
}
