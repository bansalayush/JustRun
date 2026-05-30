package com.scorpio.distancecalculator

import android.content.Intent
import android.os.Bundle
import android.os.Trace
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pomegranate.tracker.TrackerCommands.TrackerCommandFinish
import com.pomegranate.tracker.TrackerCommands.TrackerCommandPause
import com.pomegranate.tracker.TrackerCommands.TrackerCommandResume
import com.scorpio.distancecalculator.ui.theme.ColorPresets
import com.scorpio.distancecalculator.ui.theme.DualToneTheme
import com.scorpio.distancecalculator.ui.theme.composables.HomeScreen
import com.scorpio.distancecalculator.ui.theme.composables.RunStatsScreen
import com.scorpio.distancecalculator.ui.theme.composables.Screen
import dagger.hilt.android.AndroidEntryPoint

@Suppress("LongMethod")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DualToneTheme(colors = ColorPresets.Tone_Option_1) {
                val navController = rememberNavController()
                Scaffold(
                    content = { innerPadding ->
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                        ) {
                            NavHost(
                                navController = navController,
                                startDestination = Screen.Home.route,
                            ) {
                                composable(Screen.Home.route) {
                                    HomeScreen(
                                        viewModel = viewModel,
                                        navController = navController,
                                    )
                                }

                                composable(route = Screen.Tracker.route) {
                                    RunStatsScreen(
                                        viewModel = viewModel,
                                        onPlay = {
                                            try {
                                                Trace.beginSection("MainActivity: onPlay")
                                                Intent(
                                                    applicationContext,
                                                    RunningService::class.java,
                                                ).apply {
                                                    action = TrackerCommandResume.toString()
                                                }.also { this@MainActivity.startService(it) }
                                            } finally {
                                                Trace.endSection()
                                            }
                                        },
                                        onPause = {
                                            try {
                                                Trace.beginSection("MainActivity: onPause")
                                                Intent(
                                                    applicationContext,
                                                    RunningService::class.java,
                                                ).apply {
                                                    action = TrackerCommandPause.toString()
                                                }.also { this@MainActivity.startService(it) }
                                            } finally {
                                                Trace.endSection()
                                            }
                                        },
                                        onFinish = {
                                            try {
                                                Trace.beginSection("MainActivity: onFinish")
                                                Intent(
                                                    applicationContext,
                                                    RunningService::class.java,
                                                ).apply {
                                                    action = TrackerCommandFinish.toString()
                                                }.also { this@MainActivity.startService(it) }
                                            } finally {
                                                Trace.endSection()
                                            }
                                        },
                                    )
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}
