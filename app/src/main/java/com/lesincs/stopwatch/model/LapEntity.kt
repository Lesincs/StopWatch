package com.lesincs.stopwatch.model

data class LapEntity(
    val minutes: Long,
    val seconds: Long,
    val tenPercentSeconds: Long,
    val lapName: String,
    val mills: Long,
)