package com.liscman.e8calculator.calc.enums.achievements

enum class SupraregionalProvincialCompetition(val points: Int, val label: String) {
    NONE(0, " --- "),

    MULTIPLE_SUBJECT_FINALISTS(
        10,
        "Dwóch lub więcej tytułów finalisty konkursu przedmiotowego"
    ),

    MULTIPLE_THEMATIC_LAUREATES(
        7,
        "Dwóch lub więcej tytułów laureata konkursu tematycznego lub interdyscyplinarnego"
    ),

    MULTIPLE_THEMATIC_FINALISTS(
        5,
        "Dwóch lub więcej tytułów finalisty konkursu tematycznego lub interdyscyplinarnego"
    ),

    SUBJECT_FINALIST(
        7,
        "Tytuł finalisty konkursu przedmiotowego"
    ),

    THEMATIC_LAUREATE(
        3,
        "Tytuł laureata konkursu tematycznego lub interdyscyplinarnego"
    ),

    THEMATIC_FINALIST(
        2,
        "Tytuł finalisty konkursu tematycznego lub interdyscyplinarnego"
    )
}