package com.liscman.e8calculator.info

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InfoViewModel() {
    // ----- State -----
    private val _state = MutableStateFlow(InfoState())
    val state = _state.asStateFlow()

    // ----- Model -----
    val model = InfoEngine()

    // -----===== FUNCTIONS =====-----
}
