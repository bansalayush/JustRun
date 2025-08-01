package com.scorpio.distancecalculator.tracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.scorpio.distancecalculator.MainViewModel
import com.scorpio.distancecalculator.R
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStateFinished
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStatePaused
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStateActive

@Composable
fun Dp.toSp(): androidx.compose.ui.unit.TextUnit {
    val density = LocalDensity.current
    return with(density) { toPx() / density.density }.sp
}

@Composable
fun RunStatsScreen(
    viewModel: MainViewModel,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
) {
    val distance by viewModel.distanceFlow.collectAsStateWithLifecycle("0.00")
    val pace = "0.00"
    val duration by viewModel.elapsedTimeFlow.collectAsStateWithLifecycle("00:00")
    val trackingState by viewModel.trackingState.collectAsStateWithLifecycle(TrackingStateFinished)
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAF7)),
        contentAlignment = Alignment.Center
    ) {
        // Use maxWidth/maxHeight directly. Use .toSp() for text scaling.
        val bigText = (this.maxWidth * 0.2f).toSp()
        val labelText = (this.maxWidth * 0.06f).toSp()
        val unitText = (this.maxWidth * 0.055f).toSp()
        val buttonSize = (this.maxWidth * 0.22f)
        val buttonIconSize = (this.maxWidth * 0.10f)

        Box(
            Modifier
                .padding(24.dp)
                .fillMaxHeight(0.96f)
                .fillMaxWidth()
            /*.border(
                width = 8.dp,
                color = Color.Black,
                shape = RoundedCornerShape(60.dp)
            )*/
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(Modifier.height(4.dp))
                StatBlock(
                    label = "PACE",
                    value = pace,
                    unit = "min/mi",
                    labelText = labelText,
                    bigText = bigText,
                    unitText = unitText
                )
                StatBlock(
                    label = "duration",
                    value = duration,
                    labelText = labelText,
                    bigText = bigText
                )
                StatBlock(
                    label = "DISTANCE",
                    value = distance,
                    unit = "mile",
                    labelText = labelText,
                    bigText = bigText,
                    unitText = unitText
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (trackingState) {
                        TrackingStatePaused -> {
                            RoundIconButton(
                                icon = Icons.Default.PlayArrow,
                                contentDescription = "Start",
                                size = buttonSize,
                                iconSize = buttonIconSize,
                                onClick = onPlay
                            )
                            RoundIconButton(
                                icon = ImageVector.vectorResource(id = R.drawable.stop_icon),
                                contentDescription = "Finish",
                                size = buttonSize,
                                iconSize = buttonIconSize,
                                onClick = onStop
                            )
                        }

                        TrackingStateActive -> {
                            RoundIconButton(
                                icon = ImageVector.vectorResource(id = R.drawable.pause_icon),
                                contentDescription = "Pause",
                                size = buttonSize,
                                iconSize = buttonIconSize,
                                onClick = onPause
                            )
                            RoundIconButton(
                                icon = ImageVector.vectorResource(id = R.drawable.stop_icon),
                                contentDescription = "Finish",
                                size = buttonSize,
                                iconSize = buttonIconSize,
                                onClick = onStop
                            )
                        }

                        TrackingStateFinished -> RoundIconButton(
                            icon = Icons.Default.PlayArrow,
                            contentDescription = "Start",
                            size = buttonSize,
                            iconSize = buttonIconSize,
                            onClick = onPlay
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatBlock(
    label: String,
    value: String,
    unit: String? = null,
    labelText: androidx.compose.ui.unit.TextUnit,
    bigText: androidx.compose.ui.unit.TextUnit,
    unitText: androidx.compose.ui.unit.TextUnit? = null
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            fontSize = labelText,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Text(
            text = value,
            fontSize = bigText,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        if (unit != null && unitText != null) {
            Text(
                text = unit,
                fontSize = unitText,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun RoundIconButton(
    icon: ImageVector,
    contentDescription: String,
    size: Dp,
    iconSize: Dp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}
