package litle.goose.animation.component

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import litle.goose.animation.R
import litle.goose.animation.ui.theme.PreviewSurface

@Preview(showBackground = true)
@Composable
fun PreviewLottieGooseImage() = PreviewSurface {
    LottieGooseImage(Modifier.size(200.dp))
}

@Composable
fun LottieGooseImage(
    modifier: Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.goose))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

