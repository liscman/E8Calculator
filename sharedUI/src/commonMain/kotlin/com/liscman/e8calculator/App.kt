package com.liscman.e8calculator

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.liscman.e8calculator.screens.CalculatorScreen
import com.liscman.e8calculator.screens.CalendarScreen
import com.liscman.e8calculator.screens.InfoScreen
import com.liscman.e8calculator.screens.SettingsScreen
import kotlinx.coroutines.launch

/**
 * Defines the main tabs available in the application.
 * Each tab has a title and an associated [ImageVector] icon.
 */
enum class MainTab(val title: String, val icon: ImageVector) {
    Calculator("Calculator", Icons.Default.Calculate),
    Calendar("Calendar", Icons.Default.CalendarMonth),
    Info("Info", Icons.Default.Info)
}

/**
 * The root composable of the application.
 * Manages the top-level navigation state, including the bottom navigation bar,
 * the horizontal pager for switching between main tabs, and the full-screen settings view.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val calcViewModel = remember { com.liscman.e8calculator.calc.CalcViewModel() }

    MaterialTheme {
        var isSettingsOpen by remember { mutableStateOf(false) }

        if (isSettingsOpen) {
            SettingsScreen(onBack = { isSettingsOpen = false })
        } else {
            val tabs = MainTab.entries
            val pagerState = rememberPagerState(pageCount = { tabs.size })
            val scope = rememberCoroutineScope()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(tabs[pagerState.currentPage].title) },
                        actions = {
                            IconButton(onClick = { isSettingsOpen = true }) {
                                Icon(Icons.Default.Settings, contentDescription = "Settings")
                            }
                        }
                    )
                },
                bottomBar = {
                    NavigationBar {
                        tabs.forEachIndexed { index, tab ->
                            NavigationBarItem(
                                selected = pagerState.currentPage == index,
                                onClick = {
                                    scope.launch {
                                        pagerState.animateScrollToPage(index)
                                    }
                                },
                                icon = { Icon(tab.icon, contentDescription = tab.title) },
                                label = { Text(tab.title) }
                            )
                        }
                    }
                }
            ) { paddingValues ->
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) { page ->
                    when (tabs[page]) {
                        MainTab.Calculator -> CalculatorScreen(calcViewModel)
                        MainTab.Calendar -> CalendarScreen()
                        MainTab.Info -> InfoScreen()
                    }
                }
            }
        }
    }
}
