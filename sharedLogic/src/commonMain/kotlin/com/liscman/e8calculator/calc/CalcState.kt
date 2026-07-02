package com.liscman.e8calculator.calc

import com.liscman.e8calculator.calc.enums.Grade
import com.liscman.e8calculator.calc.enums.achievements.InternationalNationalCompetition
import com.liscman.e8calculator.calc.enums.achievements.OtherCompetition
import com.liscman.e8calculator.calc.enums.achievements.ProvincialCuratorCompetition
import com.liscman.e8calculator.calc.enums.achievements.SupraregionalCuratorCompetition
import com.liscman.e8calculator.calc.enums.achievements.SupraregionalProvincialCompetition

data class CalcState(
    // ----- Exam Percentages -----
    val polishExam: Int = 0, // 0-100
    val polishExamExemption: Boolean = false,
    val polishExamGrade: Grade = Grade.EMPTY, // 0, 10, 15, 25, 30, 35

    val mathExam: Int = 0, // 0-100
    val mathExamExemption: Boolean = false,
    val mathExamGrade: Grade = Grade.EMPTY, // 0, 10, 15, 25, 30, 35

    val languageExam: Int = 0, // 0-100
    val languageExamExemption: Boolean = false,
    val languageExamGrade: Grade = Grade.EMPTY, //0, 5, 10, 20, 25, 30

    // ----- Grades -----
    val polishGrade: Grade = Grade.EMPTY, // 0, 2, 8, 14, 17, 18
    val mathGrade: Grade = Grade.EMPTY, // 0, 2, 8, 14, 17, 18
    val firstAdditionalGrade: Grade = Grade.EMPTY, // 0, 2, 8, 14, 17, 18
    val secondAdditionalGrade: Grade = Grade.EMPTY, // 0, 2, 8, 14, 17, 18

    // ----- Distinction -----
    val distinction: Boolean = false, // 0/7

    // ----- Volunteering -----
    val volunteer: Boolean = false, // 0/3

    // ----- Achievements -----
    val achievementSupraregionalCurator: SupraregionalCuratorCompetition = SupraregionalCuratorCompetition.NONE,
    val achievementInternationalNational: InternationalNationalCompetition = InternationalNationalCompetition.NONE,
    val achievementProvincialCurator: ProvincialCuratorCompetition = ProvincialCuratorCompetition.NONE,
    val achievementSupraregionalProvincial: SupraregionalProvincialCompetition = SupraregionalProvincialCompetition.NONE,
    val achievementOther: OtherCompetition = OtherCompetition.NONE

)
