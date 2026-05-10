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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

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
    val angleStep = 15f  // 每张卡旋转 15°
    val baseScaleStep = 0.34f

    Canvas(modifier = modifier.graphicsLayer {
        scaleX = breathScale
        scaleY = breathScale
    }) {
        val cx = size.width * 0.65f + driftX
        val cy = size.height * 0.35f + driftY
        val baseSize = min(size.width, size.height) * 0.08f
        val cornerRadius = baseSize * 0.2f

        // 绘制远景光晕
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFAF1DF5).copy(alpha = 0.12f),
                    Color.Transparent
                ),
                center = Offset(cx, cy),
                radius = size.width * 1.6f
            ),
            radius = size.width * 1.6f,
            center = Offset(cx, cy)
        )

        // 从后往前绘制扇形矩形（使用 DrawScope.rotate 变换）
        for (i in 0 until cardCount) {
            val fraction = i.toFloat() / (cardCount - 1)
            val cardRotation = rotation + i * angleStep
            val scale = 1f + i * baseScaleStep
            val alpha = 0.14f + fraction * 0.56f

            val halfW = baseSize * scale
            val halfH = baseSize * scale
            val rectWidth = halfW * 2
            val rectHeight = halfH * 2
            val topLeft = Offset(cx - halfW, cy - halfH)

            val brush = Brush.linearGradient(
                colors = gradientColors.map { it.copy(alpha = alpha) },
                start = Offset(topLeft.x, topLeft.y + rectHeight),
                end = Offset(topLeft.x, topLeft.y),
            )

            drawContext.canvas.save()
            drawContext.canvas.translate(cx, cy)
            drawContext.canvas.rotate(cardRotation)
            drawContext.canvas.translate(-cx, -cy)
            drawRoundRect(
                brush = brush,
                topLeft = topLeft,
                size = Size(rectWidth, rectHeight),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
            )
            drawContext.canvas.restore()
        }

        // 装饰性浮动粒子
        for (i in 0 until 20) {
            val seed = i * 137.508f
            val px = sin(seed + rotation * 0.003f) * size.width * 0.45f + size.width * 0.5f
            val py = cos(seed * 0.7f + rotation * 0.002f) * size.height * 0.45f + size.height * 0.5f
            val particleAlpha = (0.08f + 0.12f * sin(seed + rotation * 0.005f)).coerceIn(0f, 1f)
            val particleSize = 1.5f.dp.toPx() + sin(seed) * 2f

            drawCircle(
                color = gradientColors[i % 3].copy(alpha = particleAlpha),
                radius = particleSize,
                center = Offset(px, py)
            )
        }

        // 装饰性几何线条
        for (i in 0 until 6) {
            val lineAngle = rotation * 0.5f + i * 60f
            val lineAlpha = 0.04f + 0.03f * sin(rotation * 0.01f + i)
            val radians = Math.toRadians(lineAngle.toDouble())
            val lineLength = size.width * 0.7f
            val startX = cx + cos(radians) * baseSize * 2
            val startY = cy + sin(radians) * baseSize * 2
            val endX = cx + cos(radians) * lineLength
            val endY = cy + sin(radians) * lineLength

            drawLine(
                color = gradientColors[i % 3].copy(alpha = lineAlpha),
                start = Offset(startX.toFloat(), startY.toFloat()),
                end = Offset(endX.toFloat(), endY.toFloat()),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 20f))
            )
        }
    }
}
