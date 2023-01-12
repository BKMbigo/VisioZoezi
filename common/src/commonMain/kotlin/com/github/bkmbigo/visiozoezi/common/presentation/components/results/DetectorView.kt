package com.github.bkmbigo.visiozoezi.common.presentation.components.results


import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.github.bkmbigo.visiozoezi.common.ml.pose.models.PoseResult
import kotlin.math.max


@Composable
fun DetectorView(
    pose: PoseResult?,
    bounds: PreviewImageBounds,
    isImageFlipped: Boolean,
    modifier: Modifier,
    edgeColor: Color = Color.White,
    landmarkColor: Color = Color.Green,
    landmarkRadius: Float = 3f,
) {
    Canvas(
        modifier = modifier,
    ) {
        if (pose != null) {
            this.drawPose(
                pose,
                edgeColor,
                landmarkColor,
                landmarkRadius,
                bounds,
                isImageFlipped
            )
        }
    }
}

private fun DrawScope.drawPose(
    pose: PoseResult,
    edgeColor: Color,
    landmarkColor: Color,
    landmarkRadius: Float,
    bounds: PreviewImageBounds,
    isImageFlipped: Boolean
) {
    pose.lines.forEach { line ->
        drawLine(
            color = edgeColor,
            start = Offset(
                bounds.toViewX(if (isImageFlipped) 1 - line.startPoint.x else line.startPoint.x),
                bounds.toViewY(line.startPoint.y)
            ),
            end = Offset(
                bounds.toViewX(if (isImageFlipped) 1 - line.endPoint.x else line.endPoint.x),
                bounds.toViewY(line.endPoint.y)
            ),
            strokeWidth = Stroke.DefaultMiter
        )
    }

    pose.points.forEach { point ->
        drawCircle(
            color = landmarkColor,
            radius = landmarkRadius,
            center = Offset(
                bounds.toViewX(if (isImageFlipped) 1 - point.x else point.x),
                bounds.toViewY(point.y)
            )
        )
    }

}


data class PreviewImageBounds(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
) {
    fun toViewX(imageX: Float) = imageX * width + x
    fun toViewY(imageY: Float) = imageY * height + y
}

fun getPreviewImageBounds(
    sourceImageWidth: Int,
    sourceImageHeight: Int,
    viewWidth: Int,
    viewHeight: Int,
//        Scale type is always FILL_CENTER in this project
): PreviewImageBounds {
    val scale =
        max(viewWidth.toFloat() / sourceImageWidth, viewHeight.toFloat() / sourceImageHeight)

    val previewImageWidth = sourceImageWidth * scale
    val previewImageHeight = sourceImageHeight * scale

    return PreviewImageBounds(
        x = viewWidth / 2 - previewImageWidth / 2,
        y = viewHeight / 2 - previewImageHeight / 2,
        width = previewImageWidth,
        height = previewImageHeight
    )
}
