package com.liscman.e8calculator.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.liscman.e8calculator.calc.CalcViewModel
import com.liscman.e8calculator.calc.CalcState
import com.liscman.e8calculator.calc.enums.Grade
import com.liscman.e8calculator.calc.enums.achievements.*

/**
 * Composable representing the Calculator screen.
 * Allows users to input exam percentages and school grades to calculate recruitment points.
 */
@Composable
fun CalculatorScreen(viewModel: CalcViewModel = remember { CalcViewModel() }) {
    val state by viewModel.state.collectAsState()
    var showResult by remember { mutableStateOf(false) }
    var resultPoints by remember { mutableStateOf(0f) }

    if (showResult) {
        ResultOverlay(points = resultPoints) { showResult = false }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Egzamin Ósmoklasisty (%)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            
            ExamInputSection(
                label = "Język polski",
                percentage = state.polishExam,
                isExempt = state.polishExamExemption,
                exemptGrade = state.polishExamGrade,
                onPercentageChange = { viewModel.updatePolishExam(it) },
                onExemptionChange = { viewModel.updatePolishExemption(it) },
                onExemptGradeChange = { viewModel.updatePolishExamGrade(it) }
            )

            ExamInputSection(
                label = "Matematyka",
                percentage = state.mathExam,
                isExempt = state.mathExamExemption,
                exemptGrade = state.mathExamGrade,
                onPercentageChange = { viewModel.updateMathExam(it) },
                onExemptionChange = { viewModel.updateMathExemption(it) },
                onExemptGradeChange = { viewModel.updateMathExamGrade(it) }
            )

            ExamInputSection(
                label = "Język obcy",
                percentage = state.languageExam,
                isExempt = state.languageExamExemption,
                exemptGrade = state.languageExamGrade,
                onPercentageChange = { viewModel.updateLanguageExam(it) },
                onExemptionChange = { viewModel.updateLanguageExemption(it) },
                onExemptGradeChange = { viewModel.updateLanguageExamGrade(it) }
            )

            HorizontalDivider()

            Text("Oceny na świadectwie", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            
            GradeDropdown("Język polski", state.polishGrade, { viewModel.updatePolishGrade(it) }, Modifier.fillMaxWidth())
            GradeDropdown("Matematyka", state.mathGrade, { viewModel.updateMathGrade(it) }, Modifier.fillMaxWidth())
            GradeDropdown("I dodatkowy przedmiot", state.firstAdditionalGrade, { viewModel.updateFirstAdditionalGrade(it) }, Modifier.fillMaxWidth())
            GradeDropdown("II dodatkowy przedmiot", state.secondAdditionalGrade, { viewModel.updateSecondAdditionalGrade(it) }, Modifier.fillMaxWidth())

            HorizontalDivider()

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.distinction, onCheckedChange = { viewModel.updateDistinction(it) })
                Text("Świadectwo z wyróżnieniem (7 pkt)")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = state.volunteer, onCheckedChange = { viewModel.updateVolunteer(it) })
                Text("Wolontariat (3 pkt)")
            }

            AchievementsSection(state, viewModel)

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (viewModel.isInputComplete()) {
                            resultPoints = viewModel.calculatePoints()
                            showResult = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = viewModel.isInputComplete()
                ) {
                    Text("SPRAWDŹ WYNIK")
                }
                OutlinedButton(
                    onClick = { viewModel.resetState() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("WYCZYŚĆ")
                }
            }
        }
    }
}

/**
 * A section for an exam subject (Polish, Math, or Foreign Language).
 * Shows either a percentage input or a grade dropdown if the student is exempt.
 * Includes a collapsible toggle for exemption status.
 */
@Composable
fun ExamInputSection(
    label: String,
    percentage: Int,
    isExempt: Boolean,
    exemptGrade: Grade,
    onPercentageChange: (Int) -> Unit,
    onExemptionChange: (Boolean) -> Unit,
    onExemptGradeChange: (Grade) -> Unit
) {
    var showExemptionDetails by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, modifier = Modifier.weight(1f))
            
            if (!isExempt) {
                OutlinedTextField(
                    value = if (percentage == 0) "" else percentage.toString(),
                    onValueChange = {
                        val filtered = it.filter { char -> char.isDigit() }
                        if (filtered.isEmpty()) {
                            onPercentageChange(0)
                        } else {
                            val newValue = filtered.toIntOrNull() ?: 0
                            if (newValue in 0..100) onPercentageChange(newValue)
                        }
                    },
                    modifier = Modifier.width(100.dp),
                    singleLine = true,
                    suffix = { Text("%") }
                )
            } else {
                GradeDropdown(
                    label = "Ocena",
                    selectedGrade = exemptGrade,
                    onGradeSelected = onExemptGradeChange,
                    modifier = Modifier.wrapContentWidth()
                )
            }

            IconButton(onClick = { showExemptionDetails = !showExemptionDetails }) {
                Icon(
                    imageVector = if (showExemptionDetails) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Opcje zwolnienia"
                )
            }
        }

        if (showExemptionDetails) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Checkbox(
                    checked = isExempt,
                    onCheckedChange = { onExemptionChange(it) }
                )
                Text("Zwolnienie z egzaminu", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

/**
 * A generic dropdown component for selecting a school [Grade].
 */
@Composable
fun GradeDropdown(
    label: String,
    selectedGrade: Grade,
    onGradeSelected: (Grade) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, modifier = Modifier.padding(end = 8.dp))
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(selectedGrade.fullName)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Grade.entries.filter { it != Grade.EMPTY }.forEach { grade ->
                    DropdownMenuItem(
                        text = { Text(grade.fullName) },
                        onClick = {
                            onGradeSelected(grade)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * A collapsible section containing various optional achievements and competition results.
 * Uses a [Surface] with a border to distinguish it as a container.
 */
@Composable
fun AchievementsSection(state: CalcState, viewModel: CalcViewModel) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            TextButton(
                onClick = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onSurface)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 8.dp),
                        )
                        Text(
                            text = "DODATKOWE OSIĄGNIĘCIA",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    if (expanded) {
                        Icon(Icons.Default.ExpandLess, contentDescription = null)
                    }
                }
            }

            if (expanded) {
                Column(
                    modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AchievementDropdown(
                        label = "Konkursy kuratoryjne (ponadwojewódzkie)",
                        selectedLabel = state.achievementSupraregionalCurator.label,
                        options = SupraregionalCuratorCompetition.entries,
                        onOptionSelected = { viewModel.updateSupraregionalCurator(it) }
                    )
                    AchievementDropdown(
                        label = "Konkursy międzynarodowe/ogólnopolskie",
                        selectedLabel = state.achievementInternationalNational.label,
                        options = InternationalNationalCompetition.entries,
                        onOptionSelected = { viewModel.updateInternationalNational(it) }
                    )
                    AchievementDropdown(
                        label = "Konkursy kuratoryjne (wojewódzkie)",
                        selectedLabel = state.achievementProvincialCurator.label,
                        options = ProvincialCuratorCompetition.entries,
                        onOptionSelected = { viewModel.updateProvincialCurator(it) }
                    )
                    AchievementDropdown(
                        label = "Inne konkursy (ponadwojewódzkie/wojewódzkie)",
                        selectedLabel = state.achievementSupraregionalProvincial.label,
                        options = SupraregionalProvincialCompetition.entries,
                        onOptionSelected = { viewModel.updateSupraregionalProvincial(it) }
                    )
                    AchievementDropdown(
                        label = "Inne osiągnięcia (artystyczne/sportowe)",
                        selectedLabel = state.achievementOther.label,
                        options = OtherCompetition.entries,
                        onOptionSelected = { viewModel.updateOtherAchievement(it) }
                    )
                }
            }
        }
    }
}

/**
 * A generic dropdown component for selecting a specific competition achievement.
 */
@Composable
fun <T> AchievementDropdown(
    label: String,
    selectedLabel: String,
    options: List<T>,
    onOptionSelected: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedLabel,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(Icons.Default.ExpandMore, contentDescription = null)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                options.forEach { option ->
                    val optionLabel = when (option) {
                        is SupraregionalCuratorCompetition -> option.label
                        is InternationalNationalCompetition -> option.label
                        is ProvincialCuratorCompetition -> option.label
                        is SupraregionalProvincialCompetition -> option.label
                        is OtherCompetition -> option.label
                        else -> option.toString()
                    }
                    DropdownMenuItem(
                        text = { Text(optionLabel) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

/**
 * A full-screen overlay that displays the final calculated recruitment points.
 * Includes an option to return to the main calculator form.
 */
@Composable
fun ResultOverlay(points: Float, onDismiss: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Twój wynik to:",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = points.toString(),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 80.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "punktów rekrutacyjnych",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(64.dp))
            
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text("POWRÓT")
            }
        }
    }
}
