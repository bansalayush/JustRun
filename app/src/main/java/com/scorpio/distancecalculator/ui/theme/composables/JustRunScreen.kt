package com.scorpio.distancecalculator.ui.theme.composables

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.scorpio.distancecalculator.MainViewModel
import com.scorpio.distancecalculator.RunningService
import com.scorpio.distancecalculator.tracker.TrackerCommands.TrackerCommandFinish
import com.scorpio.distancecalculator.tracker.TrackerCommands.TrackerCommandPause
import com.scorpio.distancecalculator.tracker.TrackerCommands.TrackerCommandResume
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStateActive
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStateFinished
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStatePaused

@Composable
fun ControlsLayout(viewmodel: MainViewModel) {
    val trackingState = viewmodel.trackingState.collectAsStateWithLifecycle(TrackingStateFinished)
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when (trackingState.value) {
            TrackingStateFinished -> {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AnimatedControlButton(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = {
                            Intent(context, RunningService::class.java).apply {
                                action = TrackerCommandResume.toString()
                            }.also { context.startService(it) }
                        },
                        icon = Icons.Default.PlayArrow,
                        text = "START",
                        backgroundColor = Color(0xFF4CAF50),
                        size = 120.dp
                    )
                }
            }

            TrackingStateActive -> {
                Box(modifier = Modifier.fillMaxWidth()) {
                    AnimatedControlButton(
                        modifier = Modifier.align(Alignment.Center),
                        onClick = {
                            Intent(context, RunningService::class.java).apply {
                                action = TrackerCommandPause.toString()
                            }.also { context.startService(it) }
                        },
                        icon = null,
                        text = "PAUSE",
                        backgroundColor = Color(0xFFFFC107),
                        size = 120.dp
                    )
                }
            }

            TrackingStatePaused -> {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedControlButton(
                        onClick = {
                            Intent(context, RunningService::class.java).apply {
                                action = TrackerCommandFinish.toString()
                            }.also { context.startService(it) }
                        },
                        icon = null,
                        text = "FINISH",
                        backgroundColor = Color(0xFFF44336),
                        size = 100.dp
                    )

                    AnimatedControlButton(
                        onClick = {
                            Intent(context, RunningService::class.java).apply {
                                action = TrackerCommandResume.toString()
                            }.also { context.startService(it) }
                        },
                        icon = Icons.Default.PlayArrow,
                        text = "RESUME",
                        backgroundColor = Color(0xFF4CAF50),
                        size = 100.dp
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedControlButton(
    onClick: () -> Unit,
    icon: ImageVector?,
    text: String,
    backgroundColor: Color,
    size: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )

    val shadowElevation by animateFloatAsState(
        targetValue = if (isPressed) 2.dp.value else 8.dp.value,
        animationSpec = tween(100),
        label = "shadow"
    )

    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .shadow(
                elevation = shadowElevation.dp,
                shape = CircleShape,
                clip = false
            )
            .background(backgroundColor, CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (icon != null)
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.4f)
                )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = (size.value * 0.12f).sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun DistanceMetricDisplay(viewModel: MainViewModel) {
    val distance by viewModel.distanceFlow.collectAsStateWithLifecycle("0.00")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            Text(
                text = distance,
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.displayLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "DISTANCE",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "kilometers",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun TimeMetricDisplay(viewModel: MainViewModel) {
    val time by viewModel.elapsedTimeFlow.collectAsStateWithLifecycle("00:00")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp)
        ) {
            Text(
                text = time.toString(),
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.displayMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "TIME",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "mm:ss",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
            )
        }
    }
}
