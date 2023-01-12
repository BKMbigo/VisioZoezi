package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

import java.awt.image.BufferedImage

actual class CameraImage actual constructor(
    val image: Any,
    val width: Int,
    val height: Int,
    val rotationDegrees: Int
) {
    actual fun close() {}

    actual companion object {
        actual class Builder(private val image: BufferedImage) {
            actual fun build(): CameraImage {
                return CameraImage(
                    image = image,
                    width = image.width,
                    height = image.height,
                    rotationDegrees = 0
                )
            }
        }
    }

}