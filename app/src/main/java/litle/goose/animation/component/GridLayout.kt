package litle.goose.animation.component

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.widget.GridLayout
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlin.random.Random

enum class Type {
    BIG_ONE,
    BIG_TWO,
    SMALL
}

data class GridItem(
    val id: String,
    val color: Color,
    val name: String,
    val type: Type
)

@Composable
fun GridCompose(
    modifier: Modifier = Modifier,
    mode: Int,
    itemList: List<GridItem>
) {
    val bigItems = remember(itemList) {
        itemList.filter { it.type == Type.BIG_ONE || it.type == Type.BIG_TWO }
    }
    val smallItems = remember(itemList) {
        itemList.filter { it.type == Type.SMALL }
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(mode),
        modifier = modifier
    ) {
        items(
            items = bigItems,
            span = { GridItemSpan(mode) }
        ) {
            GridItem(
                item = it, modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(
                        if (it.type == Type.BIG_ONE) 1.8F else 2.4F
                    )
            )
        }
        items(
            items = smallItems,
            span = { GridItemSpan(1) }
        ) {
            GridItem(
                item = it, modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1.8F)
            )
        }
    }
}

@Composable
fun GridView(
    modifier: Modifier = Modifier,
    mode: Int,
    itemList: List<GridItem>
) {
    val bigItems = remember(itemList) {
        itemList.filter { it.type == Type.BIG_ONE || it.type == Type.BIG_TWO }
    }
    val smallItems = remember(itemList) {
        itemList.filter { it.type == Type.SMALL }
    }

    val density = LocalDensity.current
    Box(modifier = modifier.verticalScroll(rememberScrollState())) {
        AndroidView(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            factory = {
                GridLayout(it).also { gridLayout ->
                    gridLayout.post {
                        val dp8 = (8 * density.density).toInt()
                        Log.d("Leon", "width: ${gridLayout.width}, height: ${gridLayout.height}")
                        gridLayout.orientation = GridLayout.VERTICAL
                        gridLayout.columnCount = mode
                        gridLayout.isRowOrderPreserved = true
                        bigItems.forEachIndexed { index, item ->
                            val view = GridItemView(gridLayout.context)
                            view.setBackgroundColor(item.color.toArgb())
                            view.text = item.name
                            val layoutParams = GridLayout.LayoutParams()
                            layoutParams.width = gridLayout.width - dp8 * 4
                            layoutParams.height =
                                (layoutParams.width * if (item.type == Type.BIG_ONE) 0.6F else 0.4F).toInt()
                            layoutParams.columnSpec = GridLayout.spec(
                                GridLayout.UNDEFINED, mode, GridLayout.CENTER, 1f
                            )
                            layoutParams.rowSpec = GridLayout.spec(index, 1)
                            gridLayout.addView(view, layoutParams)
                        }
                        smallItems.forEachIndexed { index, gridItem ->
                            val view = GridItemView(gridLayout.context)
                            view.setBackgroundColor(gridItem.color.toArgb())
                            view.text = gridItem.name
                            val layoutParams = GridLayout.LayoutParams()
                            val gridStart = index % mode
                            when (gridStart) {
                                0 -> {
                                    layoutParams.leftMargin = dp8 * 2
                                    layoutParams.rightMargin = dp8
                                    layoutParams.width = (gridLayout.width / mode) - dp8 * 3
                                }

                                mode - 1 -> {
                                    layoutParams.leftMargin = dp8
                                    layoutParams.rightMargin = dp8 * 2
                                    layoutParams.width = (gridLayout.width / mode) - dp8 * 3
                                }

                                else -> {
                                    layoutParams.leftMargin = dp8
                                    layoutParams.rightMargin = dp8
                                    layoutParams.width = (gridLayout.width / mode) - dp8 * 2
                                }
                            }
                            layoutParams.height = (layoutParams.width * 0.4F).toInt()
                            layoutParams.columnSpec = GridLayout.spec(
                                gridStart, 1, GridLayout.CENTER, 1F
                            )
                            layoutParams.rowSpec = GridLayout.spec(
                                (index / mode) + bigItems.size, 1, 1F
                            )
                            gridLayout.addView(view, layoutParams)
                        }
                    }
                }
            }
        )
    }
}

class GridItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextView(context, attrs)

@Composable
fun GridItem(
    modifier: Modifier = Modifier,
    item: GridItem
) {
    Surface(
        modifier = modifier,
        color = item.color,
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = item.name)
        }
    }
}

@Preview
@Composable
fun PreviewGridCompose() {
    GridView(
        modifier = Modifier.fillMaxSize(),
        mode = 3,
        itemList = List(20) {
            // random item
            val seed = Random.nextInt()
            GridItem(
                id = it.toString(),
                color = Color(
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255)
                ),
                name = "Item $it",
                type = when (seed % 3) {
                    0 -> Type.BIG_ONE
                    1 -> Type.BIG_TWO
                    else -> Type.SMALL
                }
            )
        }
    )
}