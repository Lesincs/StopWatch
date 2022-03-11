package com.lesincs.stopwatch.utils

fun Long.fillToAtLeast2Digital(): String {
    return String.format("%02d", this)
}

fun millsToLapComponents(mills: Long): Triple<Long, Long, Long> {
    val minutes = mills / 1000 / 60
    val seconds = mills % (1000 * 60) / 1000
    val tenPercentSeconds = mills % 1000 / 10
    return Triple(minutes, seconds, tenPercentSeconds)
}