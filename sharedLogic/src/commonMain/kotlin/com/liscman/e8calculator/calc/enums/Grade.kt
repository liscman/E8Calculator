package com.liscman.e8calculator.calc.enums

enum class Grade(
    val number: Int,
    val fullName: String,
    val points: Int,
    val examPoints: Int,
    val langExamPoints: Int,
) {
    EMPTY(0, " --- ", 0, 0, 0), // Default value
    ONE(1, "niedostateczny", 0, 0, 0),
    TWO(2, "dopuszczający", 2, 10, 5),
    THREE(3, "dostateczny", 8, 15, 10),
    FOUR(4, "dobry", 14, 25, 20),
    FIVE(5, "bardzo dobry", 17, 30, 25),
    SIX(6, "celujący", 18, 35, 30);
}