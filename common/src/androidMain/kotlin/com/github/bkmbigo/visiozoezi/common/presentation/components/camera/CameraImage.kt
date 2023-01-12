package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

import androidx.camera.core.ImageProxy

actual class CameraImage actual constructor(
    val image: Any,
    val width: Int,
    val height: Int,
    rotationDegrees: Int
) {
    actual fun close() {
        (image as ImageProxy).close()
    }

    actual companion object {
        actual class Builder(
            private val image: ImageProxy,
        ) {

            actual fun build(): CameraImage {
                return CameraImage(
                    image = image,
                    width = image.width,
                    height = image.height,
                    rotationDegrees = image.imageInfo.rotationDegrees
                )
            }
        }
    }

}