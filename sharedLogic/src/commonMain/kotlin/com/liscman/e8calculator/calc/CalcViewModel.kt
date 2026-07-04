package com.liscman.e8calculator.calc

import com.liscman.e8calculator.calc.enums.Grade
import com.liscman.e8calculator.calc.enums.achievements.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalcViewModel {
    // ----- State -----
    private val _state = MutableStateFlow(CalcState())
    val state = _state.asStateFlow()

    // ----- Model -----
    val engine = CalcEngine()

    // -----===== FUNCTIONS =====-----

    /**
     * Updates the percentage result of the Polish exam.
     * @param value Percentage value (0-100).
     */
    fun updatePolishExam(value: Int) {
        _state.value = _state.value.copy(polishExam = value)
    }

    /**
     * Updates the percentage result of the Mathematics exam.
     * @param value Percentage value (0-100).
     */
    fun updateMathExam(value: Int) {
        _state.value = _state.value.copy(mathExam = value)
    }

    /**
     * Updates the percentage result of the Foreign Language exam.
     * @param value Percentage value (0-100).
     */
    fun updateLanguageExam(value: Int) {
        _state.value = _state.value.copy(languageExam = value)
    }

    /**
     * Toggles the exemption status for the Polish exam.
     * @param value True if the student is exempt, false otherwise.
     */
    fun updatePolishExemption(value: Boolean) {
        _state.value = _state.value.copy(polishExamExemption = value)
    }

    /**
     * Toggles the exemption status for the Mathematics exam.
     * @param value True if the student is exempt, false otherwise.
     */
    fun updateMathExemption(value: Boolean) {
        _state.value = _state.value.copy(mathExamExemption = value)
    }

    /**
     * Toggles the exemption status for the Foreign Language exam.
     * @param value True if the student is exempt, false otherwise.
     */
    fun updateLanguageExemption(value: Boolean) {
        _state.value = _state.value.copy(languageExamExemption = value)
    }

    /**
     * Updates the school grade for the Polish exam (used when exempt).
     * @param grade The [Grade] received on the school certificate.
     */
    fun updatePolishExamGrade(grade: Grade) {
        _state.value = _state.value.copy(polishExamGrade = grade)
    }

    /**
     * Updates the school grade for the Mathematics exam (used when exempt).
     * @param grade The [Grade] received on the school certificate.
     */
    fun updateMathExamGrade(grade: Grade) {
        _state.value = _state.value.copy(mathExamGrade = grade)
    }

    /**
     * Updates the school grade for the Foreign Language exam (used when exempt).
     * @param grade The [Grade] received on the school certificate.
     */
    fun updateLanguageExamGrade(grade: Grade) {
        _state.value = _state.value.copy(languageExamGrade = grade)
    }

    /**
     * Updates a supra-regional competition achievement (curator level).
     */
    fun updateSupraregionalCurator(achievement: SupraregionalCuratorCompetition) {
        _state.value = _state.value.copy(achievementSupraregionalCurator = achievement)
    }

    /**
     * Updates an international or national competition achievement.
     */
    fun updateInternationalNational(achievement: InternationalNationalCompetition) {
        _state.value = _state.value.copy(achievementInternationalNational = achievement)
    }

    /**
     * Updates a provincial competition achievement (curator level).
     */
    fun updateProvincialCurator(achievement: ProvincialCuratorCompetition) {
        _state.value = _state.value.copy(achievementProvincialCurator = achievement)
    }

    /**
     * Updates a supra-regional or provincial competition achievement.
     */
    fun updateSupraregionalProvincial(achievement: SupraregionalProvincialCompetition) {
        _state.value = _state.value.copy(achievementSupraregionalProvincial = achievement)
    }

    /**
     * Updates other artistic or sports achievements.
     */
    fun updateOtherAchievement(achievement: OtherCompetition) {
        _state.value = _state.value.copy(achievementOther = achievement)
    }

    /**
     * Resets the calculator state to its default values.
     */
    fun resetState() {
        _state.value = CalcState()
    }

    /**
     * Updates the school grade for Polish.
     */
    fun updatePolishGrade(grade: Grade) {
        _state.value = _state.value.copy(polishGrade = grade)
    }

    /**
     * Updates the school grade for Mathematics.
     */
    fun updateMathGrade(grade: Grade) {
        _state.value = _state.value.copy(mathGrade = grade)
    }

    /**
     * Updates the school grade for the first additional subject.
     */
    fun updateFirstAdditionalGrade(grade: Grade) {
        _state.value = _state.value.copy(firstAdditionalGrade = grade)
    }

    /**
     * Updates the school grade for the second additional subject.
     */
    fun updateSecondAdditionalGrade(grade: Grade) {
        _state.value = _state.value.copy(secondAdditionalGrade = grade)
    }

    /**
     * Updates the distinction (honor roll) status.
     */
    fun updateDistinction(value: Boolean) {
        _state.value = _state.value.copy(distinction = value)
    }

    /**
     * Updates the volunteer activity status.
     */
    fun updateVolunteer(value: Boolean) {
        _state.value = _state.value.copy(volunteer = value)
    }

    /**
     * Checks if all required inputs are filled based on current state.
     */
    fun isInputComplete(): Boolean = engine.isInputComplete(_state.value)

    /**
     * Calculates the total recruitment points based on the current state.
     */
    fun calculatePoints(): Float = engine.calculatePoints(_state.value)
}
