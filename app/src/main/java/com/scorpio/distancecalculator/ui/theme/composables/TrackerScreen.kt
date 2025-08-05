package com.scorpio.distancecalculator.ui.theme.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.scorpio.distancecalculator.MainViewModel
import com.scorpio.distancecalculator.R
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStateActive
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStateFinished
import com.scorpio.distancecalculator.tracker.TrackingState.TrackingStatePaused
import com.scorpio.distancecalculator.ui.theme.Tone_Option_1

@Composable
fun Dp.toSp(): TextUnit {
    val density = LocalDensity.current
    return with(density) { toPx() / density.density }.sp
}

@OptIn(ExperimentalPermissionsApi::class)
@Suppress("LongMethod")
@Composable
fun RunStatsScreen(
    viewModel: MainViewModel,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onFinish: () -> Unit,
) {
    val permissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.POST_NOTIFICATIONS,
        ),
        onPermissionsResult = {

        }
    )
    LaunchedEffect(Unit) {
        permissions.launchMultiplePermissionRequest()
    }
    val distance by viewModel.distanceFlow.collectAsStateWithLifecycle("0.00")
    val pace = "0.00"
    val duration by viewModel.elapsedTimeFlow.collectAsStateWithLifecycle("00:00")
    val trackingState by viewModel.trackingState.collectAsStateWithLifecycle(TrackingStateFinished)
    BoxWithConstraints(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Tone_Option_1.background),
        contentAlignment = Alignment.Center,
    ) {
        val bigText = (this.maxWidth * 0.2f).toSp()
        val labelText = (this.maxWidth * 0.06f).toSp()
        val unitText = (this.maxWidth * 0.055f).toSp()
        val buttonSize = (this.maxWidth * 0.22f)
        val buttonIconSize = (this.maxWidth * 0.10f)

        Box(
            Modifier
                .padding(24.dp)
                .fillMaxHeight(0.96f)
                .fillMaxWidth(),
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Spacer(Modifier.height(4.dp))
                StatBlock(
                    label = "PACE",
                    value = pace,
                    unit = "m/s",
                    labelText = labelText,
                    bigText = bigText,
                    unitText = unitText,
                )
                StatBlock(
                    label = "DURATION",
                    value = duration,
                    labelText = labelText,
                    bigText = bigText,
                )
                StatBlock(
                    label = "DISTANCE",
                    value = distance,
                    unit = "kms",
                    labelText = labelText,
                    bigText = bigText,
                    unitText = unitText,
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    when (trackingState) {
                        TrackingStatePaused -> {
                            RoundIconButton(
                                icon = Icons.Default.PlayArrow,
                                contentDescription = "Start",
                                size = buttonSize,
                                iconSize = buttonIconSize,
                                onClick = onPlay,
                            )
                            RoundIconButton(
                                icon = ImageVector.vectorResource(id = R.drawable.stop_icon),
                                contentDescription = "Finish",
                                size = buttonSize,
                                iconSize = buttonIconSize,
                                onClick = onFinish,
                            )
                        }

                        TrackingStateActive -> {
                            RoundIconButton(
                                icon = ImageVector.vectorResource(id = R.drawable.pause_icon),
                                contentDescription = "Pause",
                                size = buttonSize,
                                iconSize = buttonIconSize,
                                onClick = onPause,
                            )
                            RoundIconButton(
                                icon = ImageVector.vectorResource(id = R.drawable.stop_icon),
                                contentDescription = "Finish",
                                size = buttonSize,
                                iconSize = buttonIconSize,
                                onClick = onFinish,
                            )
                        }

                        TrackingStateFinished ->
                            RoundIconButton(
                                icon = Icons.Default.PlayArrow,
                                contentDescription = "Start",
                                size = buttonSize,
                                iconSize = buttonIconSize,
                                onClick = onPlay,
                            )
                    }
                }
            }
        }
    }
}

@Suppress("LongParameterList")
@Composable
fun StatBlock(
    label: String,
    value: String,
    unit: String? = null,
    labelText: TextUnit,
    bigText: TextUnit,
    unitText: TextUnit? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(vertical = 6.dp),
    ) {
        Text(
            text = label,
            fontSize = labelText,
            fontWeight = FontWeight.Normal,
            color = Tone_Option_1.foreground,
            textAlign = TextAlign.Center,
        )
        Text(
            text = value,
            fontSize = bigText,
            fontWeight = FontWeight.Bold,
            color = Tone_Option_1.foreground,
            textAlign = TextAlign.Center,
        )
        if (unit != null && unitText != null) {
            Text(
                text = unit,
                fontSize = unitText,
                fontWeight = FontWeight.Medium,
                color = Tone_Option_1.foreground,
                textAlign = TextAlign.Center,
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
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(containerColor = Tone_Option_1.foreground),
        contentPadding = PaddingValues(0.dp),
        modifier =
            Modifier
                .size(size)
                .clip(CircleShape),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = Tone_Option_1.background,
            modifier = Modifier.size(iconSize),
        )
    }
}
