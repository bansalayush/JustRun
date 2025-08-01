package com.scorpio.distancecalculator.ui.theme.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

val listOfActivities =
    listOf<String>(
        "Running",
        "Cycling",
        "Walking",
        "Hiking",
        "Swimming",
        "Yoga",
        "Dancing",
        "Weightlifting",
        "Pilates",
        "Crossfit",
        "Running",
        "Cycling",
        "Walking",
        "Hiking",
        "Swimming",
        "Yoga",
        "Dancing",
        "Weightlifting",
        "Pilates",
        "Crossfit",
        "Running",
        "Cycling",
        "Walking",
        "Hiking",
        "Swimming",
        "Yoga",
        "Dancing",
        "Weightlifting",
        "Pilates",
        "Crossfit",
    )

@Composable
fun HomeScreen(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            LazyColumn {
                items(
                    listOfActivities.size,
                    itemContent = {
                        Text(
                            text = listOfActivities[it],
                            modifier = Modifier.padding(16.dp),
                        )
                    },
                    key = { index -> index },
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

// Background: #FAFAFA
// Cards: #F8F8F8
// Primary Text: #2C2C2C
// Secondary Text: #666666
// Borders: #E0E0E0
// Icons: #8A8A8A

@Preview
@Composable
fun ActivityItem() {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(88.dp)
                .background(Color.Blue)
                .padding(horizontal = 16.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .background(Color.Transparent)
                    .border(2.dp, Color(0xFFE0E0E0)),
        )
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
        ) {
//            Text()
        }
    }
}
