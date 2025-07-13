package com.scorpio.distancecalculator.ui.theme.composables

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.scorpio.distancecalculator.MainViewModel
import com.scorpio.distancecalculator.RunningService
import com.scorpio.distancecalculator.tracker.TrackerCommands
import com.scorpio.distancecalculator.tracker.TrackingState

@Composable
fun ControlsLayout(viewmodel: MainViewModel) {
    val trackingState = viewmodel.trackingState.collectAsStateWithLifecycle(TrackingState.finished)
    val context = LocalContext.current
    Row {
        when (trackingState.value) {
            TrackingState.finished -> {
                Box(
                    modifier = Modifier
                        .clickable {
                            Intent(context, RunningService::class.java).apply {
                                action = TrackerCommands.resume.toString()
                            }.also { context.startService(it) }
//                            viewmodel.resume()
                        }
                        .height(100f.dp)
                        .width(100f.dp)
                        .background(
                            Color(0xFFAAF683), // Soft green
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "START"
                    )
                }
            }

            TrackingState.active -> {
                Box(
                    modifier = Modifier
                        .clickable {
                            Intent(context, RunningService::class.java).apply {
                                action = TrackerCommands.pause.toString()
                            }.also { context.startService(it) }
//                            viewmodel.pause()
                        }
                        .height(100f.dp)
                        .width(100f.dp)
                        .background(
                            Color(0xFFFFF6A3), // Soft yellow
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center) {
                    Text("PAUSE")
                }
            }

            TrackingState.paused -> {
                Row {
                    Box(
                        modifier = Modifier
                            .clickable {
                                Intent(context, RunningService::class.java).apply {
                                    action = TrackerCommands.finish.toString()
                                }.also { context.startService(it) }
//                            viewmodel.finish()
                            }
                            .height(100f.dp)
                            .width(100f.dp)
                            .background(
                                Color(0xFFFFB3B3), // Soft red
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center) {
                        Text("FINISH")
                    }

                    Box(
                        modifier = Modifier
                            .clickable {
                                Intent(context, RunningService::class.java).apply {
                                    action = TrackerCommands.resume.toString()
                                }.also { context.startService(it) }
//                            viewmodel.resume()
                            }
                            .height(100f.dp)
                            .width(100f.dp)
                            .background(
                                Color(0xFFAAF683), // Soft green
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center) {
                        Text("RESUME")
                    }
                }
            }
        }
    }
}


@Composable
fun DistanceMetricDisplay(viewModel: MainViewModel) {
    val distance by viewModel.distanceFlow.collectAsStateWithLifecycle("0.00")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp) // Padding for each metric block
    ) {
        Text(
            text = distance,
            fontSize = 80.sp, // Large font size for the value
            fontWeight = FontWeight.Bold,
            color = Color.Black // Assuming black text from the image
        )
        Text(
            text = "DISTANCE(in kms)",
            fontSize = 20.sp, // Smaller font size for the label
            color = Color.Gray // Gray color for the label
        )
    }
}

@Composable
fun TimeMetricDisplay(viewModel: MainViewModel) {
    val time by viewModel.elapsedTimeFlow.collectAsStateWithLifecycle("00:00")
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp) // Padding for each metric block
    ) {
        Text(
            text = time.toString(),
            fontSize = 60.sp, // Large font size for the value
            fontWeight = FontWeight.Bold,
            color = Color.Black // Assuming black text from the image
        )
        Text(
            text = "Time(in mm:ss)",
            fontSize = 20.sp, // Smaller font size for the label
            color = Color.Gray // Gray color for the label
        )
    }
}