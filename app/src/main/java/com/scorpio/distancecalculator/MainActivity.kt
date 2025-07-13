package com.scorpio.distancecalculator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.scorpio.distancecalculator.ui.theme.DistanceCalculatorTheme
import com.scorpio.distancecalculator.ui.theme.composables.ControlsLayout
import com.scorpio.distancecalculator.ui.theme.composables.DistanceMetricDisplay
import com.scorpio.distancecalculator.ui.theme.composables.TimeMetricDisplay
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            DistanceCalculatorTheme {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .navigationBarsPadding()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        DistanceMetricDisplay(
                            viewModel = viewModel
                        )

                        // Time Section
                        TimeMetricDisplay(
                            viewModel
                        )

                        ControlsLayout(viewModel)

                        // Speed Section
//                        MetricDisplay(
//                            value = "0.0",
//                            label = "SPEED"
//                        )
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d(intent?.extras?.toString())
    }
}