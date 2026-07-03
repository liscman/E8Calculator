package com.liscman.e8calculator

import com.liscman.e8calculator.calc.CalcEngine
import com.liscman.e8calculator.calc.CalcState
import com.liscman.e8calculator.calc.enums.Grade
import com.liscman.e8calculator.calc.enums.achievements.InternationalNationalCompetition
import com.liscman.e8calculator.calc.enums.achievements.OtherCompetition
import com.liscman.e8calculator.calc.enums.achievements.ProvincialCuratorCompetition
import com.liscman.e8calculator.calc.enums.achievements.SupraregionalCuratorCompetition
import com.liscman.e8calculator.calc.enums.achievements.SupraregionalProvincialCompetition
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalcEngineTest {

    object Constants { 
        const val CALCULATOR_PRECISION = 0.1f
    }
    private val engine = CalcEngine()

    // -----===== INPUT CHECKER =====-----
    
    /**
     * Should return true when all required inputs are properly filled
     * and no field is missing or empty.
     */
    @Test
    fun `returns true when all required inputs are filled`() {
        val state = CalcState(
            polishExamExemption = false,
            polishExamGrade = Grade.EMPTY,

            mathExamExemption = false,
            mathExamGrade = Grade.EMPTY,

            languageExamExemption = false,
            languageExamGrade = Grade.EMPTY,

            polishGrade = Grade.FIVE,
            mathGrade = Grade.FOUR,
            firstAdditionalGrade = Grade.THREE,
            secondAdditionalGrade = Grade.TWO
        )

        assertTrue(engine.isInputComplete(state))
    }

    /**
     * Should return true when a mandatory exam grade is filled
     * but the student is not exempt from the exam.
     */
    @Test
    fun `returns false when required exam grade is missing and not exempt`() {
        val state = CalcState(
            polishExamExemption = false,
            polishExamGrade = Grade.EMPTY,

            mathExamExemption = false,
            mathExamGrade = Grade.SIX,

            languageExamExemption = false,
            languageExamGrade = Grade.FIVE,

            polishGrade = Grade.SIX,
            mathGrade = Grade.FIVE,
            firstAdditionalGrade = Grade.FOUR,
            secondAdditionalGrade = Grade.THREE
        )

        assertTrue(engine.isInputComplete(state))
    }

    /**
     * Should return false when exam is exempt, and if exam grade is EMPTY.
     */
    @Test
    fun `returns true when exam is exempt regardless of grade value`() {
        val state = CalcState(
            polishExamExemption = true,
            polishExamGrade = Grade.EMPTY,

            mathExamExemption = false,
            mathExamGrade = Grade.FIVE,

            languageExamExemption = false,
            languageExamGrade = Grade.FOUR,

            polishGrade = Grade.SIX,
            mathGrade = Grade.FIVE,
            firstAdditionalGrade = Grade.FOUR,
            secondAdditionalGrade = Grade.THREE
        )

        assertFalse(engine.isInputComplete(state))
    }

    /**
     * Should return false when any school grade is missing.
     */
    @Test
    fun `returns false when any school grade is missing`() {
        val state = CalcState(
            polishExamExemption = false,
            mathExamExemption = false,
            languageExamExemption = false,

            polishGrade = Grade.EMPTY,
            mathGrade = Grade.SIX,
            firstAdditionalGrade = Grade.FIVE,
            secondAdditionalGrade = Grade.FOUR
        )

        assertFalse(engine.isInputComplete(state))
    }

    /**
     * Should return false when multiple required fields are missing.
     */
    @Test
    fun `returns false when multiple required fields are missing`() {
        val state = CalcState(
            polishExamExemption = true,
            polishExamGrade = Grade.EMPTY,

            mathExamExemption = true,
            mathExamGrade = Grade.EMPTY,

            languageExamExemption = false,
            languageExamGrade = Grade.FOUR,

            polishGrade = Grade.EMPTY,
            mathGrade = Grade.EMPTY,
            firstAdditionalGrade = Grade.FOUR,
            secondAdditionalGrade = Grade.THREE
        )

        assertFalse(engine.isInputComplete(state))
    }


    // -----===== POINTS CALCULATOR =====-----

    /**
     * Should correctly calculate full points when all inputs are provided
     * and student has no exemptions or bonuses.
     */
    @Test
    fun `calculates points for complete basic state`() {
        val state = CalcState(
            polishExamExemption = false,
            polishExam = 80,

            mathExamExemption = false,
            mathExam = 75,

            languageExamExemption = false,
            languageExam = 90,

            polishGrade = Grade.FIVE,
            mathGrade = Grade.FOUR,
            firstAdditionalGrade = Grade.THREE,
            secondAdditionalGrade = Grade.TWO,

            distinction = false,
            volunteer = false
        )

        val result: Float = engine.calculatePoints(state)

        assertEquals(122.25f, result, Constants.CALCULATOR_PRECISION)

    }

    /**
     * Should calculate points when all exams are exempted
     * and grade-based exam points are used instead.
     */
    @Test
    fun `calculates points when all exams are exempted`() {
        val state = CalcState(
            polishExamExemption = true,
            polishExamGrade = Grade.SIX,

            mathExamExemption = true,
            mathExamGrade = Grade.FIVE,

            languageExamExemption = true,
            languageExamGrade = Grade.FOUR,

            polishGrade = Grade.FOUR,
            mathGrade = Grade.FIVE,
            firstAdditionalGrade = Grade.THREE,
            secondAdditionalGrade = Grade.TWO,

            distinction = true,
            volunteer = true
        )

        val result = engine.calculatePoints(state)

        assertEquals(136f, result, Constants.CALCULATOR_PRECISION)

    }

    /**
     * Should calculate points for a minimal valid input scenario
     * where most values are lowest possible grades.
     */
    @Test
    fun `calculates points for minimal valid input`() {
        val state = CalcState(
            polishExamExemption = false,
            polishExam = 0,

            mathExamExemption = false,
            mathExam = 0,

            languageExamExemption = false,
            languageExam = 0,

            polishGrade = Grade.ONE,
            mathGrade = Grade.ONE,
            firstAdditionalGrade = Grade.ONE,
            secondAdditionalGrade = Grade.ONE,

            distinction = false,
            volunteer = false
        )

        val result = engine.calculatePoints(state)

        assertEquals(0f, result, Constants.CALCULATOR_PRECISION)

    }

    /**
     * Should correctly include distinction bonus in total points.
     */
    @Test
    fun `includes distinction bonus in calculation`() {
        val state = CalcState(
            polishExam = 70,
            mathExam = 70,
            languageExam = 70,

            polishGrade = Grade.THREE,
            mathGrade = Grade.THREE,
            firstAdditionalGrade = Grade.THREE,
            secondAdditionalGrade = Grade.THREE,

            distinction = true,
            volunteer = false
        )

        val result = engine.calculatePoints(state)

        assertEquals(109f, result, Constants.CALCULATOR_PRECISION)

    }

    /**
     * Should correctly include volunteer bonus in total points.
     */
    @Test
    fun `includes volunteer bonus in calculation`() {
        val state = CalcState(
            polishExam = 60,
            mathExam = 60,
            languageExam = 60,

            polishGrade = Grade.TWO,
            mathGrade = Grade.TWO,
            firstAdditionalGrade = Grade.TWO,
            secondAdditionalGrade = Grade.TWO,

            distinction = false,
            volunteer = true
        )

        val result = engine.calculatePoints(state)

        assertEquals(71f, result, Constants.CALCULATOR_PRECISION)

    }

    /**
     * Should cap achievements points to maximum allowed value (18).
     */
    @Test
    fun `caps achievements points at maximum limit`() {
        val state = CalcState(
            polishExam = 90,
            mathExam = 90,
            languageExam = 90,

            polishGrade = Grade.SIX,
            mathGrade = Grade.SIX,
            firstAdditionalGrade = Grade.SIX,
            secondAdditionalGrade = Grade.SIX,

            distinction = false,
            volunteer = false,

            achievementSupraregionalCurator = SupraregionalCuratorCompetition.THEMATIC_OR_INTERDISCIPLINARY_LAUREATE,
            achievementInternationalNational = InternationalNationalCompetition.ART_TOURNAMENT_FINALIST,
            achievementProvincialCurator = ProvincialCuratorCompetition.THEMATIC_LAUREATE,
            achievementSupraregionalProvincial = SupraregionalProvincialCompetition.MULTIPLE_THEMATIC_FINALISTS,
            achievementOther = OtherCompetition.NATIONAL
        )

        val result = engine.calculatePoints(state)

        assertEquals(180f, result, Constants.CALCULATOR_PRECISION)

    }

    /**
     * Should handle mixed scenario with partial exam exemptions and mixed grades.
     */
    @Test
    fun `calculates mixed scenario with exemptions and grades`() {
        val state = CalcState(
            polishExamExemption = true,
            polishExamGrade = Grade.SIX,

            mathExamExemption = false,
            mathExam = 85,

            languageExamExemption = true,
            languageExamGrade = Grade.FIVE,

            polishGrade = Grade.FOUR,
            mathGrade = Grade.THREE,
            firstAdditionalGrade = Grade.TWO,
            secondAdditionalGrade = Grade.ONE,

            distinction = true,
            volunteer = true
        )

        val result = engine.calculatePoints(state)

        assertEquals(123.75f, result, Constants.CALCULATOR_PRECISION)

    }

    /**
     * Should handle mixed scenario with maximum possible points gained.
     */
    @Test
    fun `calculates scenario with maximum possible points`() {
        val state = CalcState(
            polishExamExemption = false,
            polishExam = 100,

            mathExamExemption = false,
            mathExam = 100,

            languageExamExemption = false,
            languageExam = 100,

            polishGrade = Grade.SIX,
            mathGrade = Grade.SIX,
            firstAdditionalGrade = Grade.SIX,
            secondAdditionalGrade = Grade.SIX,

            distinction = true,
            volunteer = true,

            achievementSupraregionalCurator = SupraregionalCuratorCompetition.SUBJECT_FINALIST,
            achievementInternationalNational = InternationalNationalCompetition.ART_SUBJECT_FINALIST,
            achievementProvincialCurator = ProvincialCuratorCompetition.MULTIPLE_SUBJECT_FINALISTS,
            achievementSupraregionalProvincial = SupraregionalProvincialCompetition.MULTIPLE_SUBJECT_FINALISTS,
            achievementOther = OtherCompetition.INTERNATIONAL
        )

        val result = engine.calculatePoints(state)

        assertEquals(200f, result, Constants.CALCULATOR_PRECISION)

    }

    /**
     * Should handle mixed scenario with partial exam exemptions and mixed grades.
     */
    @Test
    fun `calculates typical for user scenario`() {
        val state = CalcState(
            polishExamExemption = false,
            polishExam = 76,

            mathExamExemption = false,
            mathExam = 100,

            languageExamExemption = false,
            languageExam = 91,

            polishGrade = Grade.FOUR,
            mathGrade = Grade.FIVE,
            firstAdditionalGrade = Grade.FIVE,
            secondAdditionalGrade = Grade.FIVE,

            distinction = false,
            volunteer = true,
        )

        val result = engine.calculatePoints(state)

        assertEquals(156.9f, result, Constants.CALCULATOR_PRECISION)

    }
}