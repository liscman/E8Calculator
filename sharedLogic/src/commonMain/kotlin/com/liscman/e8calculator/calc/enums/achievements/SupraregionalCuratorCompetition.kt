package com.liscman.e8calculator.calc.enums.achievements

enum class SupraregionalCuratorCompetition(val points: Int, val label: String) {
    NONE(0, " --- "),

    SUBJECT_FINALIST(10, "Tytuł finalisty konkursu przedmiotowego"),

    THEMATIC_OR_INTERDISCIPLINARY_LAUREATE(
        7,
        "Tytuł laureata konkursu tematycznego lub interdyscyplinarnego"
    ),

    THEMATIC_OR_INTERDISCIPLINARY_FINALIST(
        5,
        "Tytuł finalisty konkursu tematycznego lub interdyscyplinarnego"
    )
}