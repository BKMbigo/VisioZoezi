package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

expect class CameraImage(
    image: Any,  // ImageProxy on Android and BufferedImage on JVM
    width: Int,
    height: Int,
    rotationDegrees: Int
) {

    fun close()

    companion object {
        class Builder {
            fun build(): CameraImage
        }
    }
}