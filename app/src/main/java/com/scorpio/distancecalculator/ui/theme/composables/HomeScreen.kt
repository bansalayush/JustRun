package com.scorpio.distancecalculator.ui.theme.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.scorpio.distancecalculator.MainViewModel
import com.scorpio.distancecalculator.formatDistanceToKmSimple
import com.scorpio.distancecalculator.formatDuration
import com.scorpio.distancecalculator.minPerKm
import com.scorpio.distancecalculator.seconds
import com.scorpio.distancecalculator.ui.theme.Tone_Option_1
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
) {
    val activityList by viewModel.activitiesStateFlow.collectAsStateWithLifecycle()
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            LazyColumn {
                items(
                    activityList.size,
                    itemContent = { i ->
                        ActivityItem(
                            timelabel = sdf.format(Date(activityList[i].activityId)),
                            distance = formatDistanceToKmSimple(activityList[i].distance),
                            duration = formatDuration(activityList[i].duration.seconds),
                            pace =
                                String.format(
                                    "%.2f /km",
                                    ((activityList[i].duration / 1000) / activityList[i].distance).minPerKm,
                                ),
                        )
                        if (i < activityList.size - 1) {
                            Box(
                                modifier =
                                    Modifier
                                        .height(1.dp)
                                        .fillMaxWidth()
                                        .background(Tone_Option_1.foreground),
                            )
                        }
                    },
                    key = { index -> activityList[index].activityId },
                )
            }
        }
        FloatingActionButton(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
            onClick = {
                navController.navigate(Screen.Tracker.route)
            },
        ) {
            Text("+")
        }
    }
}

val sdf = SimpleDateFormat("EEE, MMM d , h:mm a", java.util.Locale.getDefault())

@Composable
fun ActivityItem(
    timelabel: String,
    distance: String,
    duration: String,
    pace: String,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Tone_Option_1.background)
                .padding(horizontal = 18.dp, vertical = 14.dp),
    ) {
        Text(
            text = timelabel,
            color = Tone_Option_1.foreground.copy(alpha = 0.6f),
            style = MaterialTheme.typography.bodyMedium,
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Distance
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
                Text(
                    text = "$distance kms",
                    color = Tone_Option_1.foreground,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    "DISTANCE",
                    color = Tone_Option_1.foreground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            // Duration
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = duration,
                    color = Tone_Option_1.foreground,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    "DURATION",
                    color = Tone_Option_1.foreground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            // Pace
            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                Text(
                    text = pace,
                    maxLines = 1,
                    color = Tone_Option_1.foreground,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Text(
                    "PACE",
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    color = Tone_Option_1.foreground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}
