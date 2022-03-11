package com.lesincs.stopwatch.ui.pages

import com.lesincs.stopwatch.utils.millsToLapComponents
import org.junit.Test

class UtilsKtTest {

    @Test
    fun millsToLapComponents_test(){
        val mills_1 = 0L
        val mills_2 = 1 * 1000L
        val mills_3 = 60 * 1000L
        val mills_4 = 4 * 60 * 1000L
        val mills_5 = 4 * 60 * 1000L + 100
        val mills_6 = 4 * 60 * 1000L + 150
        val mills_7 = 4 * 60 * 1000L + 960
        val mills_8 = 4 * 61 * 1000L + 960

        kotlin.run {
            val (f,s,t) = millsToLapComponents(mills_1)
            assert(f == 0L)
            assert(s == 0L)
            assert(t == 0L)
        }

        kotlin.run {
            val (f,s,t) = millsToLapComponents(mills_2)
            assert(f == 0L)
            assert(s == 1L)
            assert(t == 0L)
        }

        kotlin.run {
            val (f,s,t) = millsToLapComponents(mills_3)
            assert(f == 1L)
            assert(s == 0L)
            assert(t == 0L)
        }

        kotlin.run {
            val (f,s,t) = millsToLapComponents(mills_4)
            assert(f == 4L)
            assert(s == 0L)
            assert(t == 0L)
        }

        kotlin.run {
            val (f,s,t) = millsToLapComponents(mills_5)
            assert(f == 4L)
            assert(s == 0L)
            assert(t == 10L)
        }
        kotlin.run {
            val (f,s,t) = millsToLapComponents(mills_6)
            assert(f == 4L)
            assert(s == 0L)
            assert(t == 15L)
        }
        kotlin.run {
            val (f,s,t) = millsToLapComponents(mills_7)
            assert(f == 4L)
            assert(s == 0L)
            assert(t == 96L)
        }

        kotlin.run {
            val (f,s,t) = millsToLapComponents(mills_8)
            assert(f == 4L)
            assert(s == 4L)
            assert(t == 96L)
        }
    }
}