package com.liscman.e8calculator.calc

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalcViewModel() {
    // ----- State -----
    private val _state = MutableStateFlow(CalcState())
    val state = _state.asStateFlow()

    // ----- Model -----
    val model = CalcEngine()

    // -----===== FUNCTIONS =====-----
}
