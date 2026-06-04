package com.scorpio.distancecalculator.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

data class DualTone(
    val background: Color,
    val foreground: Color,
    val toneId: Long,
    val displayName: String,
)

object ColorPresets {
    val Tone_Option_1 = DualTone(Color(0xffF9EBDE), Color(0xff815854), 0L, "Option 1")
    val Tone_Option_2 = DualTone(Color(0xFFFAFAF7), Color.Black, 1L, "Option 2")
    val Tone_Option_3 = DualTone(Color.Black, Color(0xFFFAFAF7), 2L, "Option 3")
    val Tone_Option_4 = DualTone(Color(0xff6A7BA2), Color(0xffFFDFDE), 3L, "Option 4")
    val listOfTones = listOf(Tone_Option_1, Tone_Option_2, Tone_Option_3, Tone_Option_4)
}
