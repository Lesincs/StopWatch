package com.lesincs.stopwatch

import com.lesincs.stopwatch.model.LapEntitiesState
import com.lesincs.stopwatch.model.LapEntity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lesincs.stopwatch.utils.millsToLapComponents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicLong

class StopWatchViewModel : ViewModel() {
    var darkTheme = MutableStateFlow(false)
    var minutes = MutableStateFlow(0L)
    var seconds = MutableStateFlow(0L)
    var tenPercentSeconds = MutableStateFlow(0L)
    var lapEntityState = MutableStateFlow(LapEntitiesState())
    var isStart = MutableStateFlow(false)
    var timerJob: Job? = null
    private val totalCumulate: AtomicLong = AtomicLong(0)
    private var currentCumulate = AtomicLong(0)

    private fun updateCurrentTime() {
        val (f, s, t) = millsToLapComponents(totalCumulate.get())
        minutes.value = f
        seconds.value = s
        tenPercentSeconds.value = t
    }

    fun onStart() {
        isStart.value = true
        timerJob = viewModelScope.launch(Dispatchers.Unconfined) {
            val interval = 10L
            while (true) {
                delay(interval)
                totalCumulate.getAndAdd(interval)
                currentCumulate.getAndAdd(interval)
                updateCurrentTime()
            }
        }
    }

    fun onReset() {
        timerJob?.cancel()

        isStart.value = false
        totalCumulate.set(0)
        currentCumulate.set(0)
        updateCurrentTime()
        lapEntityState.value = LapEntitiesState()
    }

    fun onStop() {
        timerJob?.cancel()
        isStart.value = false
    }

    fun onLap() {
        viewModelScope.launch(Dispatchers.Default) {
            lapEntityState.value = generateNewLapEntityState(old = lapEntityState.value)
        }
    }

    private fun generateNewLapEntityState(old: LapEntitiesState): LapEntitiesState {
        val newLapEntity = consumeCurrentCumulateToNewLapEntity()
        val newLapEntities = old.lapEntities.toMutableList().apply {
            add(0, newLapEntity)
        }
        val (newLongestEntity, newShortestEntity) = needSort(old.lapEntities.size + 1).run {
            if (this) {
                newLapEntities.sortedBy { it.mills }.run {
                    first() to last()
                }
            } else {
                null to null
            }
        }
        return LapEntitiesState(newLapEntities, newLongestEntity, newShortestEntity)
    }

    private fun needSort(newSize: Int): Boolean {
        return newSize >= 2
    }

    private fun consumeCurrentCumulateToNewLapEntity(): LapEntity {
        val currentCumulateValue = currentCumulate.get()
        val (f, s, t) = millsToLapComponents(currentCumulateValue)
        currentCumulate.set(0)
        return LapEntity(f, s, t, nextLapName(), currentCumulateValue)
    }

    private fun nextLapName(): String {
        return "Lap " + lapEntityState.value.lapEntities.size
    }

    fun changeTheme() {
        darkTheme.value = !darkTheme.value
    }
}