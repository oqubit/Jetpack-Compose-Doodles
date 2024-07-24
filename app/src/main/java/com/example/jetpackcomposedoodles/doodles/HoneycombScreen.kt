package com.example.jetpackcomposedoodles.doodles

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import androidx.graphics.shapes.toPath
import kotlin.math.sqrt

// @PreviewScreenSizes
/**
 * Same as @PreviewScreenSizes, but with showSystemUi disabled.
 * Currently showSystemUi adds extra empty space only visible in Preview mode,
 * which messes with the spacing of auto-sized hex tiles (Option 1),
 * especially in Landscape mode, as the extra empty space is likely making
 * screenWidthDp incorrect, which hex-tile layout spacing relies on.
 */
@Preview(name = "Phone", device = "spec:id=reference_phone,shape=Normal,width=411,height=891,unit=dp,dpi=420", showSystemUi = false)
@Preview(name = "Phone - Landscape", device = "spec:width=411dp, height=891dp, orientation=landscape, dpi=420", showSystemUi = false)
@Preview(name = "Unfolded Foldable", device = "spec:id=reference_foldable,shape=Normal,width=673,height=841,unit=dp,dpi=420", showSystemUi = false)
@Preview(name = "Tablet", device = "spec:id=reference_tablet,shape=Normal,width=1280,height=800,unit=dp,dpi=240", showSystemUi = false)
@Preview(name = "Desktop", device = "spec:id=reference_desktop,shape=Normal,width=1920,height=1080,unit=dp,dpi=160", showSystemUi = false)

@Composable
fun HoneycombScreen() {
    var sliderGapSize by remember { mutableFloatStateOf(7f) }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            /**
             * A slider for debugging purposes. o_Q
             */
            // Slider(
            //     modifier = Modifier
            //         .fillMaxWidth()
            //         .padding(
            //             start = 30.dp,
            //             end = 30.dp,
            //             top = 30.dp
            //         ),
            //     value = sliderGapSize,
            //     onValueChange = { sliderGapSize = it },
            //     valueRange = 0f..300f
            // )
            // Text(
            //     text = "Gap size: " + String.format("%.2f", sliderGapSize),
            //     modifier = Modifier.offset(y = -(15.dp))
            // )
            key(sliderGapSize) { // Forces recomposition on slider value change
                HoneycombUI(sliderGapSize)
            }
        }
    }
}

@Composable
fun HoneycombUI(
    sliderGapSize: Float,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.toFloat()
    val gapSize = sliderGapSize.coerceIn(0f, screenWidth / 7f)
    val hexSizeMax = screenWidth / 2f + (screenWidth / 7f / 2f - gapSize)

    /**---------------------------------------------------------------------------------------------
     * Option 1: Auto sizing hex tiles to the screen
     */
    // val hexSize = hexSizeMax
    // val spacingWidth = (hexSize + gapSize) * 2f

    /**---------------------------------------------------------------------------------------------
     * Option 2: Fixed size hex tiles
     */
    val hexSize = 200f.coerceIn(0f, hexSizeMax)
    val spacingWidth = 3f / 4f * (hexSize + gapSize) * 2.333f
    /** ^^^^^^^^^^^^
     * 7 screen divides (made by 2 hex columns)
     * 2.333f includes the 7th divide (2/6 = 0.333...)
     * For all things hexagons, read here: https://www.redblobgames.com/grids/hexagons
     * o_Q
     */

    val hexWidthHalf = hexSize / 2f
    val hexHeightHalf = sqrt(3f) * hexWidthHalf / 2f
    val hexWidthHeightDiff = hexWidthHalf - hexHeightHalf

    Surface(
        color = Color(0xFFFFF3D2)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement =
            Arrangement.spacedBy((-hexWidthHalf - hexWidthHeightDiff + gapSize).dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(15) {
                HexagonRounded(it, hexSize.dp, spacingWidth.dp)
            }
        }
    }
}

class MorphPolygonShape(
    private val morph: Morph,
    private val percentage: Float
) : Shape {
    private val matrix = Matrix()
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        matrix.scale(size.width / 2f, size.height / 2f)
        matrix.translate(1f, 1f)
        val path = morph.toPath(progress = percentage).asComposePath()
        path.transform(matrix)
        return Outline.Generic(path)
    }
}

@Composable
fun HexagonRounded(
    item: Int,
    hexSize: Dp,
    spacingWidth: Dp
) {
    val shapeA = remember {
        RoundedPolygon(
            6,
            rounding = CornerRounding(0.2f)
        )
    }
    val shapeB = remember {
        RoundedPolygon.star(
            6,
            rounding = CornerRounding(0.1f)
        )
    }
    val morph = remember {
        Morph(shapeA, shapeB)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animatedProgress = animateFloatAsState(
        targetValue = if (isPressed) 1f else 0f,
        label = "progress",
        animationSpec = spring(dampingRatio = 0.4f, stiffness = Spring.StiffnessMedium)
    )
    Row(
        modifier = Modifier
            .width(spacingWidth)
            .wrapContentWidth(if (item % 2 == 0) Alignment.Start else Alignment.End)
    ) {
        Box(
            modifier = Modifier
                .size(hexSize)
                .clip(MorphPolygonShape(morph, animatedProgress.value))
                .background(Color(0xFFFFC20A))
                .clickable(interactionSource = interactionSource, indication = null) {
                }
        ) {
            Text(
                "Item $item",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}