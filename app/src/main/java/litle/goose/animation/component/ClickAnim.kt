package litle.goose.animation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import litle.goose.animation.ui.theme.PreviewSurface

@Preview
@Composable
private fun PreviewClickAnim() = PreviewSurface {
    ClickAnim()
}

@Composable
fun ClickAnim() {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 水波纹反馈按钮
        DefaultButton()
        // 颜色反馈按钮
        ChangeColorButton()
        // 圆角反馈按钮
        ChangeCornerButton()
        // 缩放反馈按钮
        ChangeScaleButton()
        // 阴影反馈按钮
        ChangeElevationButton()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultButton() {
    Card(onClick = {}) {
        Text(text = "ripple", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ChangeColorButton() {
    val buttonInteractionSource = remember { MutableInteractionSource() }
    val isPressed by buttonInteractionSource.collectIsPressedAsState()
    val buttonColor by animateColorAsState(
        targetValue = if (isPressed) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "button 2 color"
    )
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = buttonInteractionSource,
                indication = null,
                onClick = {}
            )
            .clip(RoundedCornerShape(12.dp))
            .background(color = buttonColor),
    ) {
        Text(text = "color", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ChangeCornerButton() {
    val buttonInteractionSource = remember { MutableInteractionSource() }
    val isPressed by buttonInteractionSource.collectIsPressedAsState()
    val buttonCorner by animateDpAsState(
        targetValue = if (isPressed) 36.dp else 12.dp,
        animationSpec = tween(80),
        label = "button 3 corner"
    )
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = buttonInteractionSource,
                indication = null,
                onClick = {}
            )
            .clip(RoundedCornerShape(buttonCorner))
            .background(color = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Text(text = "corner", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ChangeScaleButton() {
    val buttonInteractionSource = remember { MutableInteractionSource() }
    val isPressed by buttonInteractionSource.collectIsPressedAsState()
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.86F else 1F,
        animationSpec = tween(80),
        label = "button 4 scale"
    )
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = buttonInteractionSource,
                indication = null,
                onClick = {}
            )
            .scale(buttonScale)
            .clip(RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Text(text = "Scale", modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun ChangeElevationButton() {
    val buttonInteractionSource = remember { MutableInteractionSource() }
    val isPressed by buttonInteractionSource.collectIsPressedAsState()
    val buttonElevation by animateDpAsState(
        targetValue = if (isPressed) 2.dp else 6.dp,
        animationSpec = tween(80),
        label = "button 5 elevation"
    )
    Box(
        modifier = Modifier
            .clickable(
                interactionSource = buttonInteractionSource,
                indication = null,
                onClick = {}
            )
            .shadow(elevation = buttonElevation, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Text(text = "Elevation", modifier = Modifier.padding(16.dp))
    }
}

