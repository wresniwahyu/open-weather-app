package com.weather.app.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GradientLinearIndicator(
    progress: Float,
    gradientColors: List<Color>,
    modifier: Modifier = Modifier,
    height: Dp = 4.dp
) {
    Canvas(
        modifier = Modifier
            .height(height)
            .clip(MaterialTheme.shapes.medium)
            .then(modifier)
    ) {
        val gradient = Brush.linearGradient(
            colors = gradientColors,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f)
        )

        drawRect(
            brush = gradient,
            size = Size(progress * size.width, size.height)
        )
    }
}

@Composable
fun LinearIndicatorWithGradientColor(
    modifier: Modifier = Modifier,
    progress: Float
) {
    val gradientColors = listOf(
        Color(0xFF1E88E5),  // Start color
        Color(0xFFF4511E)   // End color
    )

    GradientLinearIndicator(
        progress = progress,
        gradientColors = gradientColors,
        modifier = modifier.width(100.dp)
    )
}

@Preview
@Composable
fun LinearIndicatorWithGradientColorPrev() {
    LinearIndicatorWithGradientColor(progress = 100f)
}
