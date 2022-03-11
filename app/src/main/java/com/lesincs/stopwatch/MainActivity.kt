package com.lesincs.stopwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lesincs.stopwatch.ui.DigitalPage
import com.lesincs.stopwatch.theme.StopWatchTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val stopWatchViewModel = viewModel<StopWatchViewModel>()
            val darkTheme by stopWatchViewModel.darkTheme.collectAsState()
            StopWatchTimerTheme(darkTheme = darkTheme) {
                DigitalPage()
            }
        }
    }
}
