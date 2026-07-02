package com.liscman.e8calculator.calc.enums.achievements

enum class InternationalNationalCompetition(val points: Int, val label: String) {
    NONE(0, " --- "),

    ART_SUBJECT_FINALIST(
        10,
        "Tytuł finalisty konkursu z przedmiotu artystycznego"
    ),

    ART_TOURNAMENT_LAUREATE(
        4,
        "Tytuł laureata turnieju artystycznego (nieobjętego ramowym planem)"
    ),

    ART_TOURNAMENT_FINALIST(
        3,
        "Tytuł finalisty turnieju artystycznego (nieobjętego ramowym planem)"
    )
}