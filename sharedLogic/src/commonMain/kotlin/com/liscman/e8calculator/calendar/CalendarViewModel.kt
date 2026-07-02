package com.liscman.e8calculator.calendar

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalendarViewModel() {
    // ----- State -----
    private val _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    // ----- Model -----
    val model = CalendarEngine()

    // -----===== FUNCTIONS =====-----
}
