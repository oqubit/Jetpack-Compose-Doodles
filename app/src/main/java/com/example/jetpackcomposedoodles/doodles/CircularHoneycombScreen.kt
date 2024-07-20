package com.example.jetpackcomposedoodles.doodles

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.roundToIntRect
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.Morph
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.star
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularHoneycombScreen() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.toFloat()
    val screenHeight = configuration.screenHeightDp.toFloat()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF3D2)),
            contentAlignment = Alignment.Center
        ) {
            HexagonRoundedCentered(item = 0, hexSize = 165.dp)
            Row {
                CircularLayout(
                    modifier = Modifier.weight(1f, false),
                    radius = screenWidth.coerceAtMost(screenHeight),
                    content = {
                        (1..3).map { HexagonRoundedCentered(item = it, hexSize = 100.dp) }
                    }
                )
            }
        }
    }
}

@Composable
fun CircularLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    radius: Float = 250f
) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val angularSeparation = 360 / placeables.size
        val boundedRectangle = Rect(
            center = Offset(
                x = 0f,
                y = 0f,
            ),
            radius = radius + placeables.first().height,
        ).roundToIntRect()
        val center = IntOffset(boundedRectangle.width / 2, boundedRectangle.height / 2)

        layout(boundedRectangle.width, boundedRectangle.height) {
            var requiredAngle = 180.0
            placeables.forEach { placeable ->
                val x = center.x + (radius * sin(Math.toRadians(requiredAngle))).toInt()
                val y = center.y + (radius * cos(Math.toRadians(requiredAngle))).toInt()
                placeable.placeRelative(x - placeable.width / 2, y - placeable.height / 2)
                requiredAngle -= angularSeparation
            }
        }
    }
}

@Composable
fun HexagonRoundedCentered(
    item: Int,
    hexSize: Dp
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