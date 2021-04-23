package com.example.androiddevchallenge.earthquake

interface IEarthquakeScope {
    fun startShaking()
    fun stopShaking()
    val isShaking: Boolean
    var shakeDuration: Long
    var shakesPerSecond: Int
    var shakeForce: Int
}

class EarthquakeScope(private val state: EarthquakeState) : IEarthquakeScope {
    override fun startShaking() {
        if (!state.isShaking && state.earthquakeDuration > 0)
            state.isShaking = true
    }

    override fun stopShaking() {
        if (state.isShaking)
            state.isShaking = false
    }

    override val isShaking: Boolean
        get() = state.isShaking

    override var shakeDuration: Long = state.earthquakeDuration
        set(value) {
            field = value
            state.earthquakeDuration = value.coerceAtLeast(0)
        }

    override var shakesPerSecond: Int = state.shakesPerSecond
        set(value) {
            field = value
            state.shakesPerSecond = value.coerceAtLeast(1)
        }

    override var shakeForce: Int = state.shakeForce
        set(value) {
            field = value
            state.shakeForce = value.coerceAtLeast(1)
        }
}