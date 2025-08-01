package com.scorpio.distancecalculator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.scorpio.distancecalculator.tracker.RunStatsScreen
import com.scorpio.distancecalculator.tracker.TrackerCommands.TrackerCommandFinish
import com.scorpio.distancecalculator.tracker.TrackerCommands.TrackerCommandPause
import com.scorpio.distancecalculator.tracker.TrackerCommands.TrackerCommandResume
import com.scorpio.distancecalculator.ui.theme.DistanceCalculatorTheme
import com.scorpio.distancecalculator.ui.theme.Tone_Option_1
import com.scorpio.distancecalculator.ui.theme.composables.HomeScreen
import com.scorpio.distancecalculator.ui.theme.composables.Screen
import timber.log.Timber

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DistanceCalculatorTheme {
                val navController = rememberNavController()
                Surface {
                    Scaffold(
                        modifier =
                            Modifier
                                .fillMaxHeight(1.0f)
                                .fillMaxWidth()
                                .border(
                                    width = 24.dp,
                                    color = Tone_Option_1.foreground,
                                ),
                        content = { paddingValues ->
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(paddingValues),
                            ) {
                                NavHost(
                                    navController = navController,
                                    startDestination = Screen.Tracker.route,
                                ) {
                                    composable(Screen.Home.route) {
                                        HomeScreen(
                                            viewModel = viewModel,
                                            navController = navController,
                                        )
                                    }

                                    composable(route = Screen.Tracker.route) {
                                        RunStatsScreen(viewModel = viewModel, {
                                            Intent(
                                                this@MainActivity,
                                                RunningService::class.java,
                                            ).apply {
                                                action = TrackerCommandResume.toString()
                                            }.also { this@MainActivity.startService(it) }
                                        }, {
                                            Intent(
                                                this@MainActivity,
                                                RunningService::class.java,
                                            ).apply {
                                                action = TrackerCommandPause.toString()
                                            }.also { this@MainActivity.startService(it) }
                                        }, {
                                            Intent(
                                                this@MainActivity,
                                                RunningService::class.java,
                                            ).apply {
                                                action = TrackerCommandFinish.toString()
                                            }.also { this@MainActivity.startService(it) }
                                        })
                                    }
                                }
                            }
                        },
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Timber.d(intent?.extras?.toString())
    }
}
