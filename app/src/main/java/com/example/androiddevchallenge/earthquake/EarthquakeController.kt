package com.example.androiddevchallenge.earthquake

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Immutable
interface IEarthquakeController {
    fun startShaking(earthquakeDuration: Long, shakeDuration: Long, shakeForce: Int)
    fun stopShaking()
}

@Immutable
class EarthquakeController(
    private val scope: CoroutineScope,
    private val mover: IEarthquakeMover,
    private val onEarthquakeFinished: () -> Unit
) : IEarthquakeController {
    private var timerJob: Job? = null

    override fun startShaking(earthquakeDuration: Long, shakeDuration: Long, shakeForce: Int) {
        timerJob = scope.flowTimer(
            duration = earthquakeDuration,
            period = shakeDuration,
            onFinished = {
                onEarthquakeFinished()
                stopShaking()
            }
        ) {
            scope.launch {
                mover.move(
                    shakeDuration = shakeDuration.toInt(),
                    shakeForce = shakeForce
                )
            }
        }
    }

    override fun stopShaking() {
        timerJob ?: return

        timerJob?.cancel()
        timerJob = null

        scope.launch {
            mover.stop()
        }

        onEarthquakeFinished()
    }
}