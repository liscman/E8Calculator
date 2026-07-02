package com.liscman.e8calculator.calc.enums.achievements

enum class OtherCompetition(val points: Int, val label: String) {
    NONE(0, " --- "),

    INTERNATIONAL(4, "Miejsce międzynarodowe"),

    NATIONAL(3, "Miejsce krajowe"),

    PROVINCIAL(2, "Miejsce wojewódzkie"),

    DISTRICT(1, "Miejsce powiatowe")
}