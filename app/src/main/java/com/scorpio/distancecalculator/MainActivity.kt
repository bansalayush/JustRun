package com.scorpio.distancecalculator

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.times
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.scorpio.distancecalculator.tracker.RunStatsScreen
import com.scorpio.distancecalculator.tracker.TrackerCommands.TrackerCommandFinish
import com.scorpio.distancecalculator.tracker.TrackerCommands.TrackerCommandPause
import com.scorpio.distancecalculator.tracker.TrackerCommands.TrackerCommandResume
import com.scorpio.distancecalculator.ui.theme.DistanceCalculatorTheme
import com.scorpio.distancecalculator.ui.theme.composables.HomeScreen
import com.scorpio.distancecalculator.ui.theme.composables.Screen
import timber.log.Timber

@Suppress("LongMethod")
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DistanceCalculatorTheme {
                val navController = rememberNavController()
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .statusBarsPadding()
                            .navigationBarsPadding(),
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
                            RunStatsScreen(viewModel = viewModel, {
                                Intent(
                                    applicationContext,
                                    RunningService::class.java,
                                ).apply {
                                    action = TrackerCommandResume.toString()
                                }.also { this@MainActivity.startService(it) }
                            }, {
                                Intent(
                                    applicationContext,
                                    RunningService::class.java,
                                ).apply {
                                    action = TrackerCommandPause.toString()
                                }.also { this@MainActivity.startService(it) }
                            }, {
                                Intent(
                                    applicationContext,
                                    RunningService::class.java,
                                ).apply {
                                    action = TrackerCommandFinish.toString()
                                }.also { this@MainActivity.startService(it) }
                            })
                        }
                    }
                }
            }
        }
    }
}
