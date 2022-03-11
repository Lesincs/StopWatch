package com.lesincs.stopwatch.ui

import com.lesincs.stopwatch.model.LapEntity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lesincs.stopwatch.StopWatchViewModel
import com.lesincs.stopwatch.utils.fillToAtLeast2Digital

@Composable
internal fun DigitalPage() {
    val viewModel = viewModel<StopWatchViewModel>()
    StatusBarSetting()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Stop Watch") },
                actions = {
                    IconButton(onClick = viewModel::changeTheme) {
                        Icon(Icons.Filled.BrightnessMedium, contentDescription = "")
                    }
                }
            )
        },
    ) {
        DigitalPageContent()
    }
}

@Composable
private fun DigitalPageContent() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimeEntrance()
        Divider()
        LapListEntrance(
            modifier = Modifier
                .padding(10.dp)
                .weight(1f)
        )
        BottomTriggersEntrance()
    }
}

@Composable
private fun BottomTriggersEntrance() {
    val viewModel = viewModel<StopWatchViewModel>()

    val isStart by viewModel.isStart.collectAsState()
    if (isStart) {
        BottomTriggers(
            onLeftTriggerClicked = viewModel::onLap,
            onRightTriggerClicked = viewModel::onStop,
            leftText = "Lap",
            rightText = "Stop",
            leftColor = Color(73, 73, 75),
            rightColor = Color.Red
        )
    } else {
        BottomTriggers(
            onLeftTriggerClicked = viewModel::onReset,
            onRightTriggerClicked = viewModel::onStart,
            leftText = "Reset",
            rightText = "Start",
            leftColor = Color(73, 73, 75),
            rightColor = Color(101, 219, 118)
        )
    }
}

@Composable
private fun LapListEntrance(modifier: Modifier = Modifier) {
    val viewModel = viewModel<StopWatchViewModel>()
    val lapEntityState = viewModel.lapEntityState.collectAsState().value
    LazyColumn(modifier = modifier) {
        items(items = lapEntityState.lapEntities) {
            LapItem(
                it,
                isLongest = it == lapEntityState.longestLapEntity,
                isShortest = it == lapEntityState.shortestLapEntity
            )
            Divider()
        }
    }
}

@Composable
private fun TimeEntrance() {
    val viewModel = viewModel<StopWatchViewModel>()
    val seconds by viewModel.seconds.collectAsState()
    val minutes by viewModel.minutes.collectAsState()
    val tenPercentSeconds by viewModel.tenPercentSeconds.collectAsState()
    Time(
        seconds = seconds,
        minutes = minutes,
        tenPercentSeconds = tenPercentSeconds
    )
}

@Composable
private fun StatusBarSetting() {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = if (MaterialTheme.colors.isLight) {
        MaterialTheme.colors.primaryVariant
    } else MaterialTheme.colors.surface
    SideEffect {
        systemUiController.setSystemBarsColor(color = statusBarColor, darkIcons = false)
    }

}

@Composable
private fun LapItem(
    lapEntity: LapEntity,
    isLongest: Boolean = false,
    isShortest: Boolean = false,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp)
    ) {
        Text(
            text = lapEntity.lapName,
            modifier = Modifier.align(Alignment.CenterStart),
            style = MaterialTheme.typography.h6
        )
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (isLongest) {
                Spacer(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                )
            }
            if (isShortest) {
                Spacer(
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
            }
            Text(
                text = lapEntity.minutes.fillToAtLeast2Digital(),
                style = MaterialTheme.typography.h6
            )
            Text(text = ":", style = MaterialTheme.typography.h6)
            Text(
                text = lapEntity.seconds.fillToAtLeast2Digital(),
                style = MaterialTheme.typography.h6
            )
            Text(text = ".", style = MaterialTheme.typography.h6)
            Text(
                text = lapEntity.tenPercentSeconds.fillToAtLeast2Digital(),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
private fun Time(seconds: Long, minutes: Long, tenPercentSeconds: Long) {
    Row(modifier = Modifier.padding(10.dp)) {
        val fontSize = 60.sp
        Text(
            text = minutes.fillToAtLeast2Digital(), fontSize = fontSize,
            style = MaterialTheme.typography.h1
        )
        Text(text = ":", fontSize = fontSize, style = MaterialTheme.typography.h1)
        Text(
            text = seconds.fillToAtLeast2Digital(),
            fontSize = fontSize,
            style = MaterialTheme.typography.h1
        )
        Text(text = ":", fontSize = fontSize, style = MaterialTheme.typography.h1)
        Text(
            text = tenPercentSeconds.fillToAtLeast2Digital(),
            fontSize = fontSize,
            style = MaterialTheme.typography.h1
        )
    }
}

@Composable
private fun BottomTriggers(
    onLeftTriggerClicked: () -> Unit,
    onRightTriggerClicked: () -> Unit,
    leftText: String,
    rightText: String,
    leftColor: Color,
    rightColor: Color,
) {
    val buttonHeight = 60.dp
    Row(Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp)) {
        val buttonBaseModifier = Modifier
            .weight(1f)
            .height(buttonHeight)
        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = leftColor),
            onClick = onLeftTriggerClicked,
            shape = RoundedCornerShape(
                topStart = 30.dp,
                bottomStart = 30.dp,
                topEnd = 2.dp,
                bottomEnd = 2.dp
            ),
            modifier = buttonBaseModifier
        ) {
            Text(text = leftText, color = Color.White)
        }

        Spacer(modifier = Modifier.width(4.dp))

        Button(
            colors = ButtonDefaults.buttonColors(backgroundColor = rightColor),
            onClick = onRightTriggerClicked,
            shape = RoundedCornerShape(
                topStart = 2.dp,
                bottomStart = 2.dp,
                topEnd = 30.dp,
                bottomEnd = 30.dp
            ),
            modifier = buttonBaseModifier
        ) {
            Text(text = rightText)
        }
    }
}

