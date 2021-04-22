package com.example.androiddevchallenge.earthquake

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import kotlin.random.Random

private const val BackToNormalDuration = 150

@Immutable
interface IEarthquakeMover {
    val x: Animatable<Float, AnimationVector1D>
    val y: Animatable<Float, AnimationVector1D>
    val rotation: Animatable<Float, AnimationVector1D>
    val alpha: Animatable<Float, AnimationVector1D>

    suspend fun move(shakeDuration: Int, shakeForce: Int)
    suspend fun stop()

    fun generateOffset(shakeForce: Int): Offset
    fun generateRotation(shakeForce: Int): Float
    fun generateAlpha(): Float
}

@Immutable
class EarthquakeMover : IEarthquakeMover {
    override val x = Animatable(0.dp.value)
    override val y = Animatable(0.dp.value)
    override val rotation = Animatable(0.dp.value)
    override val alpha = Animatable(1f)

    override suspend fun move(shakeDuration: Int, shakeForce: Int) {
        val shakeOffset = generateOffset(shakeForce)
        val shakeRotation = generateRotation(shakeForce)
        val shakeAlpha = generateAlpha()

        x.animateTo(
            targetValue = shakeOffset.x,
            animationSpec = tween(shakeDuration)
        )

        y.animateTo(
            targetValue = shakeOffset.y,
            animationSpec = tween(shakeDuration)
        )

        rotation.animateTo(
            targetValue = shakeRotation,
            animationSpec = tween(shakeDuration)
        )

        alpha.animateTo(
            targetValue = shakeAlpha,
            animationSpec = tween(shakeDuration)
        )
    }

    override suspend fun stop() {
        x.animateTo(
            targetValue = 0.dp.value,
            animationSpec = tween(BackToNormalDuration)
        )

        y.animateTo(
            targetValue = 0.dp.value,
            animationSpec = tween(BackToNormalDuration)
        )

        rotation.animateTo(
            targetValue = 0.dp.value,
            animationSpec = tween(BackToNormalDuration)
        )

        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(BackToNormalDuration)
        )

        x.stop()
        y.stop()
        rotation.stop()
        alpha.stop()
    }

    override fun generateOffset(shakeForce: Int): Offset {
        val x = Random.nextInt(-shakeForce, shakeForce)
        val y = Random.nextInt(-shakeForce, shakeForce)
        return Offset(x.toFloat(), y.toFloat())
    }

    override fun generateRotation(shakeForce: Int) =
        Random.nextInt(-shakeForce, shakeForce).toFloat()

    override fun generateAlpha() = Random.nextFloat().coerceAtLeast(.5f)
}