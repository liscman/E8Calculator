package com.liscman.e8calculator.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Composable representing the Calculator screen.
 * Currently displays a placeholder text.
 * TODO Implement calculator functionality
 */
@Composable
fun CalculatorScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("CalculatorScreen")
    }
}