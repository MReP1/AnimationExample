package litle.goose.animation.component

import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import litle.goose.animation.R
import litle.goose.animation.ui.theme.PreviewSurface

@Preview
@Composable
fun PreviewFrameAnimGooseImage() = PreviewSurface {
    FrameAnimGooseImageView()
}

@Composable
fun FrameAnimGooseImageView() {
    AndroidView(factory = { ImageView(it) }) { imageView ->
        imageView.setImageResource(R.drawable.anim_little_goose)
        (imageView.drawable as AnimationDrawable).start()
    }
}

@Composable
fun FrameAnimGooseImage() {
    val normalGoose = painterResource(id = R.drawable.ic_little_goose)
    val backGoose = painterResource(id = R.drawable.ic_little_goose_back)
    val foreGoose = painterResource(id = R.drawable.ic_little_goose_fore)
    var painter by remember { mutableStateOf(normalGoose) }
    Image(painter = painter, contentDescription = "Loading")
    LaunchedEffect(Unit) {
        delay(80)
        while (true) {
            painter = backGoose
            delay(100)
            painter = normalGoose
            delay(80)
            painter = foreGoose
            delay(100)
            painter = normalGoose
            delay(80)
        }
    }
}

