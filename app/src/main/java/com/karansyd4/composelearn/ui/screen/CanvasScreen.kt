package com.karansyd4.composelearn.ui.screen

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun PreviewCanvasScreen() {
    CanvasScreen()
}

@Composable
fun CanvasScreen() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val paint = Paint()
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 64f
        paint.color = 0xffff0000.toInt()
        paint.typeface = Typeface.MONOSPACE
        drawIntoCanvas {
            it.nativeCanvas.drawText("Text Here", center.x, center.y + 400f, paint)
        }
        drawLine(
            color = Color.Magenta,
            start = Offset(canvasWidth, 0f),
            end = Offset(0f, canvasWidth),
            strokeWidth = 5f
        )
        drawCircle(
            color = Color.Black,
            center = Offset(canvasWidth / 2, canvasHeight / 2),
            radius = 100f
        )
        drawCircle(
            color = Color.DarkGray,
            center = Offset(canvasWidth / 2, canvasHeight / 3 + 1000f),
            radius = 100f,
            style = Stroke(width = 20f)
        )
        rotate(degrees = 30F) {
            drawRect(
                topLeft = Offset(canvasWidth / 2, canvasHeight / 4),
                color = Color.Blue,
                size = size / 8f,
            )
        }
        drawRoundRect(
            color = Color.Magenta,
            size = Size(width = 100f, height = 400f),
            cornerRadius = CornerRadius(50f, 50f),
            topLeft = Offset(canvasWidth / 4, canvasHeight / 4)
        )
        val canvasQuadrantSize = size / 2F
        inset(50F, 30F) {
            drawRect(
                color = Color.Green,
                size = canvasQuadrantSize / 4f
            )
        }

        val trianglePath = Path().apply {
            // Moves to top center position
            moveTo(size.width / 4f, 0f)
            // Add line to bottom right corner
            lineTo(size.width / 4f, size.width / 4f)
            // Add line to bottom left corner
            lineTo(0f, size.width / 4f)
        }
        drawPath(
            color = Color.Green,
            path = trianglePath
        )
    }
}