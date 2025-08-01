package com.scorpio.distancecalculator.ui.theme.composables

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Tracker : Screen("tracker")
    // Add more as needed
}