package com.liscman.e8calculator

import com.liscman.e8calculator.calc.CalcEngine
import com.liscman.e8calculator.calc.CalcState
import com.liscman.e8calculator.calc.enums.Grade
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalcEngineTest {

    private val engine = CalcEngine()

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
}