package com.github.bkmbigo.visiozoezi.common.presentation.components.camera

actual class CameraImage actual constructor(
    image: Any,
    width: Int,
    height: Int,
    rotationDegrees: Int
) {
    actual fun close() {
    }

    actual companion object {
        actual class Builder {
            actual fun build(): CameraImage {
                TODO("Not yet implemented")
            }
        }
    }

}