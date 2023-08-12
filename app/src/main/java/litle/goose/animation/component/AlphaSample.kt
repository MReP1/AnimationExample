package litle.goose.animation.component

import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import litle.goose.animation.R
import litle.goose.animation.ui.theme.PreviewSurface

@Preview(showBackground = true)
@Composable
private fun PreviewViewAlphaSample() = PreviewSurface {
    Surface(modifier = Modifier.fillMaxSize()) {
        ViewAlphaSample(modifier = Modifier.size(200.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewComposeAlphaSample() = PreviewSurface {
    ComposeAlphaSample(modifier = Modifier.size(200.dp))
}

@Composable
fun ViewAlphaSample(modifier: Modifier) {
    AndroidView(
        factory = { context ->
            // 新建一个展示图片的 ImageView
            ImageView(context).apply {
                setImageResource(R.drawable.ic_little_goose)
            }
        },
        modifier = modifier
    ) { image ->
        // 新建一个动画辅助类，设置在两秒内将动画数值从 1 慢慢变成 0 再变成1
        val animator = ValueAnimator.ofFloat(1F, 0F, 1F).apply {
            duration = 2000L
            interpolator = AccelerateDecelerateInterpolator()
        }.also {
            it.start()
        }
        // 监听数值变化，在每一次变化都调整 ImageView 的透明度
        animator.addUpdateListener { a ->
            // 每帧回调一次
            image.alpha = a.animatedValue as Float
        }
    }
}

@Composable
fun ComposeAlphaSample(modifier: Modifier) {
    // alpha 中的 value 值不断改变触发这个函数重组
    val alpha = remember { Animatable(1F) }
    Image(
        painter = painterResource(id = R.drawable.ic_little_goose),
        contentDescription = "little goose",
        modifier = modifier.alpha(alpha.value)
    )
    LaunchedEffect(Unit) {
        alpha.animateTo(0F, tween(2000, easing = FastOutSlowInEasing))
        alpha.animateTo(1F, tween(2000, easing = FastOutSlowInEasing))
    }
}
