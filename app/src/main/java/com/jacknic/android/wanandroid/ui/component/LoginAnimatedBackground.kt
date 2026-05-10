package com.jacknic.android.wanandroid.ui.component

import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.min
import kotlin.random.Random

/**
 * JetBrains 风格动画背景 — 扇形展开的圆角矩形 + 渐变
 * 独立 Composable，不影响表单交互与布局性能
 */
@Composable
fun LoginAnimatedBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "bg_transition")

    // 整体缓慢旋转
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 40_000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Restart
        ),
        label = "bg_rotation"
    )

    // 呼吸缩放
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6_000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_breath"
    )

    // 粒子漂移偏移
    val driftX by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12_000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_drift_x"
    )
    val driftY by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9_000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bg_drift_y"
    )

    // JetBrains 品牌渐变色
    val gradientColors = listOf(
        Color(0xFFAF1DF5),  // 紫
        Color(0xFFFE2857),  // 粉红
        Color(0xFFFC801D),  // 橙
    )

    // 扇形矩形参数
    val cardCount = 13
    val angleStep = 20f
    val baseScaleStep = 0.34f

    // 每张卡的随机分散偏移（固定种子，避免重组时抖动）
    val cardOffsets = remember {
        val rng = Random(42)
        List(cardCount) {
            Offset(rng.nextFloat(), rng.nextFloat())
        }
    }

    Canvas(
        modifier = modifier
            .blur(20.dp)
            .graphicsLayer {
                scaleX = breathScale
                scaleY = breathScale
            }) {
        val cx = size.width * 0.65f + driftX
        val cy = size.height * 0.35f + driftY
        val baseSize = min(size.width, size.height) * 0.08f

        for (i in 0 until cardCount) {
            val fraction = i.toFloat() / (cardCount - 1)
            val cardRotation = rotation + i * angleStep
            val scale = 1f + i * baseScaleStep
            val alpha = 0.14f + fraction * 0.56f

            val offset = cardOffsets[i]
            val cardCx = cx * offset.x
            val cardCy = cy * offset.y
            val radius = baseSize * scale

            val brush = Brush.linearGradient(
                colors = gradientColors.map { it.copy(alpha = alpha) },
                start = Offset(cardCx - radius, cardCy + radius),
                end = Offset(cardCx + radius, cardCy - radius),
            )

            drawContext.canvas.save()
            drawContext.canvas.translate(cardCx, cardCy)
            drawContext.canvas.rotate(cardRotation)
            drawContext.canvas.translate(-cardCx, -cardCy)
            drawCircle(
                brush = brush,
                radius = radius,
                center = Offset(cardCx, cardCy),
            )
            drawContext.canvas.restore()
        }
    }
}
