package com.scorpio.benchmark

import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.MemoryUsageMetric
import androidx.benchmark.macro.MemoryUsageMetric.SubMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TrackerStartBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun startTrackerAndRecord10Seconds() = benchmarkRule.measureRepeated(
        packageName = "com.scorpio.distancecalculator",
        metrics = listOf(
            MemoryUsageMetric(
                mode = MemoryUsageMetric.Mode.Max,
                subMetrics = listOf(SubMetric.HeapSize,)
            )
        ),
        iterations = 3,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()

        device.wait(Until.hasObject(By.desc("Sprint Forward")), 5_000)
        // Click FloatingActionButton (Sprint Forward icon)
        val fab = device.findObject(By.desc("Sprint Forward"))
        fab?.click()

        // Wait for TrackerScreen to appear (look for "Start" button)
        device.wait(Until.hasObject(By.desc("Start")), 5_000)

        // Click the "Start" RoundIconButton
        val startButton = device.findObject(By.desc("Start"))
        startButton?.click()

        // Wait up to 15 seconds for the DURATION text to become "00:10"
        device.wait(Until.hasObject(By.text("00:10")), 10_000)

        device.wait(Until.hasObject(By.desc("Finish")), 5_000)

// Click the "Finish" RoundIconButton
        val finishButton = device.findObject(By.desc("Finish"))
        finishButton?.click()
    }
}