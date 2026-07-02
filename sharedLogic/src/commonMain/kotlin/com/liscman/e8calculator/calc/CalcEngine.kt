package com.liscman.e8calculator.calc

import com.liscman.e8calculator.calc.enums.Grade

class CalcEngine{
    // -----===== CHECK INPUT =====-----
    /**
     * Checks whether all required inputs for the calculation are filled.
     * - If a student is NOT exempt from the exam, a valid grade must be provided.
     * - All required subjects must have a selected grade (cannot be EMPTY).
     * @return true if all necessary fields are completed, false otherwise.
     */
    fun isInputComplete(state: CalcState): Boolean{
        state.run {
            return when {
                //Exam exemption
                polishExamExemption && polishExamGrade == Grade.EMPTY -> false
                mathExamExemption && mathExamGrade == Grade.EMPTY -> false
                languageExamExemption && languageExamGrade == Grade.EMPTY -> false

                //Grades
                polishGrade == Grade.EMPTY -> false
                mathGrade == Grade.EMPTY -> false
                firstAdditionalGrade == Grade.EMPTY -> false
                secondAdditionalGrade == Grade.EMPTY -> false

                //All correct
                else -> true
            }
        }
    }

}