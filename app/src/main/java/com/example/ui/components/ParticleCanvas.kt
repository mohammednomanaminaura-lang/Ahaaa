package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.ui.theme.*
import kotlin.random.Random

data class Particle(
    var x: Float,
    var y: Float,
    var speedY: Float,
    var speedX: Float,
    var size: Float,
    var alpha: Float,
    var color: Color
)

@Composable
fun ParticleBackground(
    modifier: Modifier = Modifier,
    particleColor: Color = ElectricNeonBlue,
    particleCount: Int = 40
) {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    val particles = remember(particleColor) {
        List(particleCount) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                speedY = 0.002f + Random.nextFloat() * 0.004f,
                speedX = (Random.nextFloat() - 0.5f) * 0.002f,
                size = 2f + Random.nextFloat() * 5f,
                alpha = 0.2f + Random.nextFloat() * 0.7f,
                color = if (Random.nextBoolean()) particleColor else NecromancerPurple
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        particles.forEach { p ->
            p.y -= p.speedY
            p.x += p.speedX
            if (p.y < 0f) {
                p.y = 1f
                p.x = Random.nextFloat()
            }
            if (p.x < 0f) p.x = 1f
            if (p.x > 1f) p.x = 0f

            val drawX = p.x * width
            val drawY = p.y * height

            // Draw glowing particle
            drawCircle(
                color = p.color.copy(alpha = p.alpha * 0.4f),
                radius = p.size * 2f,
                center = Offset(drawX, drawY)
            )
            drawCircle(
                color = p.color.copy(alpha = p.alpha),
                radius = p.size,
                center = Offset(drawX, drawY)
            )
        }
    }
}
