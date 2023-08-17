package litle.goose.animation.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.SweepGradientShader
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.toComposePaint
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import litle.goose.animation.ui.theme.PreviewSurface
import kotlin.math.roundToInt

@Composable
fun DoubleCircleAnimationButton(modifier: Modifier) {
    val outCircleProps = remember { CircleAnimationProperties() }
    val innerCircleProps = remember { CircleAnimationProperties(initAngle = 180F) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = modifier
            .defaultMinSize(minWidth = 176.dp, minHeight = 176.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { },
        contentAlignment = Alignment.Center
    ) {
        CircleAnimation(
            modifier = Modifier
                .align(Alignment.Center)
                .size(116.dp),
            isPressed = isPressed,
            properties = outCircleProps
        )

        CircleAnimation(
            modifier = Modifier
                .align(Alignment.Center)
                .size(86.dp),
            isPressed = isPressed,
            properties = innerCircleProps
        )

        Icon(imageVector = Icons.Rounded.Place, contentDescription = "AI")
    }
}

@Stable
data class CircleAnimationProperties(
    val angle: Float = 200F,
    val oneCircleTimeMillis: Int = 800,
    val headColor: Color = Color.Blue,
    val tailColor: Color = Color.Red,
    val width: Dp = 4.dp,
    val elevation: Dp = 3.dp,
    val initAngle: Float = 0F
)

@Stable
private data class CircleAnimationPaintCache(
    val emptyPaint: Paint,
    val blendPaint: Paint,
    val arcPaint: Paint
)

@Composable
fun CircleAnimation(
    modifier: Modifier,
    isPressed: Boolean,
    properties: CircleAnimationProperties = remember { CircleAnimationProperties() }
) {
    val (angle, oneCircleTimeMillis, headColor, tailColor, width, elevation, initAngle) = properties
    val startAngle = remember { Animatable(initAngle) }
    val sweepAngle = remember { Animatable(0F) }
    LaunchedEffect(isPressed) {
        coroutineScope {
            if (isPressed) {
                launch {
                    startAngle.snapTo(initAngle)
                    while (isActive) {
                        startAngle.animateTo(
                            targetValue = startAngle.value + 360F,
                            animationSpec = tween(
                                durationMillis = oneCircleTimeMillis,
                                easing = LinearEasing
                            )
                        )
                    }
                }
                launch {
                    sweepAngle.snapTo(0F)
                    sweepAngle.animateTo(
                        targetValue = angle,
                        animationSpec = tween(
                            durationMillis = oneCircleTimeMillis * 2,
                            easing = LinearEasing
                        )
                    )
                }
            } else if (sweepAngle.value > 0F) {
                val stA = startAngle.value
                val swA = sweepAngle.value
                val durationMillis =
                    (oneCircleTimeMillis * (angle / 360F) * (swA / angle)).roundToInt()
                launch {
                    startAngle.animateTo(
                        targetValue = stA + swA * 1.5F,
                        animationSpec = tween(
                            durationMillis = durationMillis,
                            easing = LinearEasing
                        )
                    )
                }
                launch {
                    sweepAngle.animateTo(
                        targetValue = 0F,
                        animationSpec = tween(
                            durationMillis = durationMillis,
                            easing = LinearEasing
                        )
                    )
                }
            }
        }
    }

    val density = LocalDensity.current
    val paintCache = remember {
        CircleAnimationPaintCache(
            emptyPaint = Paint(),
            blendPaint = Paint().apply {
                style = PaintingStyle.Fill
                blendMode = BlendMode.DstIn
            },
            arcPaint = with(density) {
                android.graphics
                    .Paint()
                    .apply {
                        setShadowLayer(elevation.toPx(), 0F, 0F, Color.Black.toArgb())
                    }.toComposePaint().apply {
                        style = PaintingStyle.Stroke
                        strokeWidth = width.toPx()
                        strokeCap = StrokeCap.Round
                        strokeJoin = StrokeJoin.Round
                    }
            }
        )
    }
    Canvas(modifier = modifier) {
        val startA = startAngle.value
        val sweepA = sweepAngle.value
        val emptyPaint = paintCache.emptyPaint
        val head = (startA + sweepA) % 360F / 360F
        val tail = (startA + sweepA - angle) % 360F / 360F
        val transparentTail = startA % 360F / 360F
        val deltaAngle = angle / 360F
        val tempColor = headColor.getRatioColor(tailColor, head / deltaAngle)
        val centerOffset = Offset(size.width / 2, size.height / 2)
        val blendPaint = paintCache.blendPaint.apply {
            shader = SweepGradientShader(
                center = centerOffset,
                colors = if (head >= transparentTail) {
                    listOf(Color.Transparent, Color.Transparent, Color.White, Color.Transparent)
                } else {
                    val tempAlphaColor = Color.White.copy(
                        alpha = (1F - transparentTail) / deltaAngle
                    )
                    listOf(
                        tempAlphaColor, Color.White, Color.Transparent, tempAlphaColor
                    )
                },
                colorStops = if (head > transparentTail) {
                    listOf(0F, transparentTail, head, 1F)
                } else {
                    listOf(0F, head, transparentTail, 1F)
                }
            )
        }
        val arcPaint = paintCache.arcPaint.apply {
            shader = SweepGradientShader(
                center = centerOffset,
                colors = if (head >= tail) {
                    listOf(tailColor, tailColor, headColor, headColor.copy(alpha = 0F))
                } else {
                    listOf(tempColor, headColor, tailColor, tempColor)
                },
                colorStops = if (head >= tail) {
                    listOf(0F, tail, head, 1F)
                } else {
                    listOf(0F, head, tail, 1F)
                }
            )
        }
        drawIntoCanvas { canvas ->
            canvas.withSaveLayer(Rect(Offset.Zero, size), emptyPaint) {
                val padding = elevation.toPx() + width.toPx() + 2.dp.toPx()
                canvas.drawArc(
                    left = padding,
                    top = padding,
                    right = size.width - padding,
                    bottom = size.height - padding,
                    startAngle = startA,
                    sweepAngle = sweepA,
                    useCenter = false,
                    paint = arcPaint
                )
                canvas.drawArc(
                    left = 0F,
                    top = 0F,
                    right = size.width,
                    bottom = size.height,
                    startAngle = startA - 10,
                    sweepAngle = sweepA + 20,
                    useCenter = true,
                    paint = blendPaint
                )
            }
        }
    }
}

private fun Color.getRatioColor(color: Color, ratio: Float): Color {
    return Color(
        red = (this.red - (this.red - color.red * color.alpha) * ratio).coerceIn(0F, 1F),
        green = (this.green - (this.green - color.green * color.alpha) * ratio).coerceIn(0F, 1F),
        blue = (this.blue - (this.blue - color.blue * color.alpha) * ratio).coerceIn(0F, 1F),
        alpha = (this.alpha - (this.alpha - color.alpha) * ratio).coerceIn(0F, 1F)
    )
}

@Preview
@Composable
fun PreviewDoubleCircleAnimation() = PreviewSurface {
    DoubleCircleAnimationButton(modifier = Modifier.size(200.dp))
}

@Preview
@Composable
fun PreviewCircleAnimation() = PreviewSurface {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    Box(
        modifier = Modifier
            .size(200.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { },
        contentAlignment = Alignment.Center
    ) {
        CircleAnimation(Modifier.size(200.dp), isPressed = isPressed)
    }
}