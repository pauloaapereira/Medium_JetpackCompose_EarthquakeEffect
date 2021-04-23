package com.example.androiddevchallenge.earthquake

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

fun CoroutineScope.flowTimer(
    duration: Long,
    period: Long,
    onFinished: () -> Unit = {},
    onTick: (Long) -> Unit
) =
    this.launch {
        (duration downTo 0 step period).asFlow()
            .onEach {
                onTick(it)
                delay(period)
            }
            .onCompletion {
                onFinished()
            }
            .collect()
    }