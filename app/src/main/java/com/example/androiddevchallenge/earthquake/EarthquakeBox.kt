package com.example.androiddevchallenge.earthquake

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

@Stable
class EarthquakeState {

    var isShaking by mutableStateOf(false)
    var earthquakeDuration by mutableStateOf(2000L)
    var shakesPerSecond by mutableStateOf(10)
    var shakeForce by mutableStateOf(10)

}

@Immutable
data class EarthquakeProperties(
    val mover: IEarthquakeMover,
    val controller: IEarthquakeController,
    val state: EarthquakeState,
    val scope: IEarthquakeScope
)

@Composable
fun EarthquakeBox(
    onEarthquakeFinished: () -> Unit = {},
    content: @Composable IEarthquakeScope.() -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    val earthquake = remember {
        val mover = EarthquakeMover()
        val state = EarthquakeState()
        val scope = EarthquakeScope(state = state)

        val controller = EarthquakeController(
            scope = coroutineScope,
            mover = mover,
            onEarthquakeFinished = {
                state.isShaking = false
                onEarthquakeFinished()
            }
        )

        EarthquakeProperties(
            mover = mover,
            controller = controller,
            state = state,
            scope = scope
        )
    }

    LaunchedEffect(earthquake.state.isShaking) {
        if (earthquake.state.isShaking) {
            earthquake.controller.startShaking(
                earthquakeDuration = earthquake.state.earthquakeDuration,
                shakeDuration = 1000L / earthquake.state.shakesPerSecond,
                shakeForce = earthquake.state.shakeForce
            )
        } else {
            earthquake.controller.stopShaking()
        }
    }

    Box(
        modifier = Modifier
            .alpha(earthquake.mover.alpha.value)
            .offset(earthquake.mover.x.value.dp, earthquake.mover.y.value.dp)
            .rotate(earthquake.mover.rotation.value)
            .padding(earthquake.state.shakeForce.dp)
    ) {
        earthquake.scope.content()
    }
}