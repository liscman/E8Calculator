package com.liscman.e8calculator.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel() {
    // ----- State -----
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    // ----- Model -----
    val model = SettingsEngine()

    // -----===== FUNCTIONS =====-----
}
