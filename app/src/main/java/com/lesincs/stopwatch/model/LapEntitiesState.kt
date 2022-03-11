package com.lesincs.stopwatch.model

data class LapEntitiesState(
    val lapEntities: List<LapEntity> = mutableListOf(),
    val shortestLapEntity: LapEntity? = null,
    val longestLapEntity: LapEntity? = null,
)