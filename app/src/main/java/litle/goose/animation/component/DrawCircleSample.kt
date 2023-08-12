package litle.goose.animation.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import litle.goose.animation.ui.theme.PreviewSurface

@Preview(showBackground = true)
@Composable
private fun PreviewDrawCircleSample() = PreviewSurface {
    DrawCircleSample(modifier = Modifier.size(200.dp))
}

@Composable
fun DrawCircleSample(modifier: Modifier = Modifier) {
    val animatedSweepAngle = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animatedSweepAngle.animateTo(
            targetValue = 360f,
            animationSpec = tween(durationMillis = 2000)
        )
    }
    Canvas(modifier) {
        drawArc(
            color = Color.Blue,
            startAngle = 0f,
            sweepAngle = animatedSweepAngle.value,
            useCenter = false,
            topLeft = Offset.Zero,
            size = this.size
        )
    }
}