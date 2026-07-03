package com.liscman.e8calculator.calc

import com.liscman.e8calculator.calc.enums.Grade
import kotlin.math.min
import kotlin.run

class CalcEngine{
    // -----===== CONSTANTS =====-----
    object Constants {
        //Exam Multipliers
        const val polishExamMultiplier = 0.35
        const val mathExamMultiplier = 0.35
        const val languageExamMultiplier = 0.3

        //Other
        const val DISTINCTION_POINTS = 7
        const val VOLUNTEERING_POINTS = 3
    }

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

    // -----===== CALCULATE POINTS =====-----
    /**
     * Calculates total recruitment points based on the provided student state.
     *
     * The calculation includes:
     * - Exam results (Polish, Math, Foreign Language)
     *   - Uses raw percentage or converted grade points depending on exemption status
     * - School grades (Polish, Math, two additional subjects)
     * - Extra achievements (capped to a maximum of 18 points)
     * - Additional bonuses:
     *   - Distinction (honor roll)
     *   - Volunteer activity
     *
     * @return total number of recruitment points as a Float
     */
    fun calculatePoints(state: CalcState): Float{
        var points = 0f
        state.run {
            //Exam points
            points +=
                if (!polishExamExemption)
                    polishExam* Constants.polishExamMultiplier.toFloat()
                else
                    polishExamGrade.examPoints.toFloat()
            points +=
                if (mathExamExemption)
                    mathExamGrade.examPoints.toFloat()
                else
                    mathExam*Constants.mathExamMultiplier.toFloat()
            points +=
                if (!languageExamExemption)
                    languageExam*Constants.languageExamMultiplier.toFloat()
                else
                    languageExamGrade.langExamPoints.toFloat()

            //Grade points
            points += polishGrade.points
            points += mathGrade.points
            points += firstAdditionalGrade.points
            points += secondAdditionalGrade.points

            //Distinction points
            if (distinction) points += Constants.DISTINCTION_POINTS

            //Volunteer points
            if (volunteer) points += Constants.VOLUNTEERING_POINTS

            //Achievements points
            var achievementsPoints = 0
            achievementsPoints += achievementSupraregionalCurator.points
            achievementsPoints += achievementInternationalNational.points
            achievementsPoints += achievementProvincialCurator.points
            achievementsPoints += achievementSupraregionalProvincial.points
            achievementsPoints += achievementOther.points
            points += min(achievementsPoints, 18)
        }
        return points
    }
}