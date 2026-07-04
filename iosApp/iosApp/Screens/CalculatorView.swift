import SwiftUI
import SharedLogic

/**
 * View representing the Calculator screen on iOS.
 * Native SwiftUI implementation that interacts with the shared Kotlin logic.
 */
struct CalculatorView: View {
    @StateObject private var obs: CalculatorObservable
    @State private var showResult = false
    @State private var resultPoints: Float = 0

    init(viewModel: CalcViewModel) {
        _obs = StateObject(wrappedValue: CalculatorObservable(viewModel: viewModel))
    }

    var body: some View {
        ZStack {
            ScrollView {
                VStack(alignment: .leading, spacing: 20) {
                    // --- Exams Section ---
                    SectionHeader(title: "Egzamin Ósmoklasisty (%)")

                    VStack(spacing: 12) {
                        ExamInputRow(
                            label: "Język polski",
                            percentage: Int(obs.state.polishExam),
                            isExempt: obs.state.polishExamExemption,
                            exemptGrade: obs.state.polishExamGrade,
                            onPercentageChange: { obs.viewModel.updatePolishExam(value: Int32($0)) },
                            onExemptionChange: { obs.viewModel.updatePolishExemption(value: $0) },
                            onExemptGradeChange: { obs.viewModel.updatePolishExamGrade(grade: $0) }
                        )

                        ExamInputRow(
                            label: "Matematyka",
                            percentage: Int(obs.state.mathExam),
                            isExempt: obs.state.mathExamExemption,
                            exemptGrade: obs.state.mathExamGrade,
                            onPercentageChange: { obs.viewModel.updateMathExam(value: Int32($0)) },
                            onExemptionChange: { obs.viewModel.updateMathExemption(value: $0) },
                            onExemptGradeChange: { obs.viewModel.updateMathExamGrade(grade: $0) }
                        )

                        ExamInputRow(
                            label: "Język obcy",
                            percentage: Int(obs.state.languageExam),
                            isExempt: obs.state.languageExamExemption,
                            exemptGrade: obs.state.languageExamGrade,
                            onPercentageChange: { obs.viewModel.updateLanguageExam(value: Int32($0)) },
                            onExemptionChange: { obs.viewModel.updateLanguageExemption(value: $0) },
                            onExemptGradeChange: { obs.viewModel.updateLanguageExamGrade(grade: $0) }
                        )
                    }

                    Divider()

                    // --- Grades Section ---
                    SectionHeader(title: "Oceny na świadectwie")

                    VStack(spacing: 12) {
                        GradePickerRow(label: "Język polski", selection: obs.state.polishGrade) {
                            obs.viewModel.updatePolishGrade(grade: $0)
                        }
                        GradePickerRow(label: "Matematyka", selection: obs.state.mathGrade) {
                            obs.viewModel.updateMathGrade(grade: $0)
                        }
                        GradePickerRow(label: "I dodatkowy przedmiot", selection: obs.state.firstAdditionalGrade) {
                            obs.viewModel.updateFirstAdditionalGrade(grade: $0)
                        }
                        GradePickerRow(label: "II dodatkowy przedmiot", selection: obs.state.secondAdditionalGrade) {
                            obs.viewModel.updateSecondAdditionalGrade(grade: $0)
                        }
                    }

                    Divider()

                    // --- Checkboxes (Toggles) ---
                    VStack(alignment: .leading, spacing: 12) {
                        Toggle("Świadectwo z wyróżnieniem (7 pkt)", isOn: Binding(
                            get: { obs.state.distinction },
                            set: { obs.viewModel.updateDistinction(value: $0) }
                        ))

                        Toggle("Wolontariat (3 pkt)", isOn: Binding(
                            get: { obs.state.volunteer },
                            set: { obs.viewModel.updateVolunteer(value: $0) }
                        ))
                    }

                    // --- Achievements ---
                    AchievementsCollapsible(obs: obs)

                    // --- Buttons ---
                    VStack(spacing: 12) {
                        Button(action: {
                            if obs.viewModel.isInputComplete() {
                                resultPoints = obs.viewModel.calculatePoints()
                                withAnimation { showResult = true }
                            }
                        }) {
                            Text("SPRAWDŹ WYNIK")
                                .font(.headline)
                                .foregroundColor(.white)
                                .frame(maxWidth: .infinity)
                                .padding()
                                .background(obs.viewModel.isInputComplete() ? Color.accentColor : Color.gray.opacity(0.3))
                                .cornerRadius(12)
                        }
                        .disabled(!obs.viewModel.isInputComplete())

                        Button(action: { obs.viewModel.resetState() }) {
                            Text("WYCZYŚĆ")
                                .font(.subheadline)
                                .foregroundColor(.accentColor)
                                .frame(maxWidth: .infinity)
                                .padding()
                                .overlay(RoundedRectangle(cornerRadius: 12).stroke(Color.accentColor, lineWidth: 1))
                        }
                    }
                    .padding(.top, 20)
                }
                .padding()
            }

            if showResult {
                ResultView(points: resultPoints) {
                    withAnimation { showResult = false }
                }
                .transition(.move(edge: .bottom))
                .zIndex(10)
            }
        }
    }
}

// MARK: - Subcomponents

struct SectionHeader: View {
    let title: String
    var body: some View {
        Text(title)
            .font(.title3)
            .fontWeight(.bold)
    }
}

struct ExamInputRow: View {
    let label: String
    let percentage: Int
    let isExempt: Bool
    let exemptGrade: Grade
    let onPercentageChange: (Int) -> Void
    let onExemptionChange: (Bool) -> Void
    let onExemptGradeChange: (Grade) -> Void

    @State private var showExemptionToggle = false

    var body: some View {
        VStack(alignment: .leading, spacing: 8) {
            HStack {
                Text(label)
                    .frame(maxWidth: .infinity, alignment: .leading)

                if !isExempt {
                    TextField("0", value: Binding(
                        get: { percentage },
                        set: {
                            let clamped = max(0, min($0, 100))
                            onPercentageChange(clamped)
                        }
                    ), formatter: NumberFormatter())
                    .keyboardType(.numberPad)
                    .textFieldStyle(.roundedBorder)
                    .frame(width: 70)
                    Text("%")
                } else {
                    GradeMenu(selection: exemptGrade, onSelect: onExemptGradeChange)
                        .frame(width: 130)
                }

                Button(action: { withAnimation { showExemptionToggle.toggle() } }) {
                    Image(systemName: showExemptionToggle ? "chevron.up.circle.fill" : "chevron.down.circle.fill")
                        .foregroundColor(.accentColor)
                }
            }

            if showExemptionToggle {
                Toggle("Zwolnienie z egzaminu", isOn: Binding(
                    get: { isExempt },
                    set: { onExemptionChange($0) }
                ))
                .font(.footnote)
                .padding(.leading, 10)
            }
        }
    }
}

struct GradePickerRow: View {
    let label: String
    let selection: Grade
    let onSelect: (Grade) -> Void

    var body: some View {
        HStack {
            Text(label)
            Spacer()
            GradeMenu(selection: selection, onSelect: onSelect)
        }
    }
}

struct GradeMenu: View {
    let selection: Grade
    let onSelect: (Grade) -> Void

    var body: some View {
        Menu {
            ForEach([Grade.one, .two, .three, .four, .five, .six], id: \.self) { grade in
                Button(grade.fullName) { onSelect(grade) }
            }
        } label: {
            HStack {
                Text(selection == .empty ? "Wybierz..." : selection.fullName)
                    .lineLimit(1)
                Image(systemName: "chevron.down")
                    .font(.caption2)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 8)
            .background(Color(UIColor.secondarySystemBackground))
            .cornerRadius(8)
        }
    }
}

struct AchievementsCollapsible: View {
    @ObservedObject var obs: CalculatorObservable
    @State private var expanded = false

    var body: some View {
        VStack(spacing: 0) {
            Button(action: { withAnimation { expanded.toggle() } }) {
                HStack {
                    Image(systemName: expanded ? "chevron.up" : "chevron.down")
                    Text("DODATKOWE OSIĄGNIĘCIA")
                        .font(.caption)
                        .fontWeight(.bold)
                    Spacer()
                }
                .foregroundColor(.primary)
                .padding()
                .background(Color(UIColor.secondarySystemBackground).opacity(0.8))
                .cornerRadius(10)
            }

            if expanded {
                VStack(spacing: 16) {
                    AchievementSelector(label: "Konkursy ponadwojewódzkie (kuratoryjne)",
                                        selection: obs.state.achievementSupraregionalCurator.label,
                                        options: SupraregionalCuratorCompetition.allCases()) {
                        obs.viewModel.updateSupraregionalCurator(achievement: $0 as! SupraregionalCuratorCompetition)
                    }

                    AchievementSelector(label: "Konkursy międzynarodowe/ogólnopolskie",
                                        selection: obs.state.achievementInternationalNational.label,
                                        options: InternationalNationalCompetition.allCases()) {
                        obs.viewModel.updateInternationalNational(achievement: $0 as! InternationalNationalCompetition)
                    }

                    AchievementSelector(label: "Konkursy wojewódzkie (kuratoryjne)",
                                        selection: obs.state.achievementProvincialCurator.label,
                                        options: ProvincialCuratorCompetition.allCases()) {
                        obs.viewModel.updateProvincialCurator(achievement: $0 as! ProvincialCuratorCompetition)
                    }

                    AchievementSelector(label: "Inne konkursy (ponadwojewódzkie/wojewódzkie)",
                                        selection: obs.state.achievementSupraregionalProvincial.label,
                                        options: SupraregionalProvincialCompetition.allCases()) {
                        obs.viewModel.updateSupraregionalProvincial(achievement: $0 as! SupraregionalProvincialCompetition)
                    }

                    AchievementSelector(label: "Inne osiągnięcia (artystyczne/sportowe)",
                                        selection: obs.state.achievementOther.label,
                                        options: OtherCompetition.allCases()) {
                        obs.viewModel.updateOtherAchievement(achievement: $0 as! OtherCompetition)
                    }
                }
                .padding()
                .background(Color(UIColor.secondarySystemBackground).opacity(0.4))
                .cornerRadius(10)
                .padding(.top, 4)
            }
        }
    }
}

struct AchievementSelector: View {
    let label: String
    let selection: String
    let options: [Any]
    let onSelect: (Any) -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            Text(label).font(.caption2).foregroundColor(.secondary)
            Menu {
                ForEach(0..<options.count, id: \.self) { i in
                    let opt = options[i]
                    let title = (opt as? HasLabel)?.label ?? "---"
                    Button(title) { onSelect(opt) }
                }
            } label: {
                HStack {
                    Text(selection).lineLimit(1).font(.body)
                    Spacer()
                    Image(systemName: "chevron.down").font(.caption)
                }
                .padding(10)
                .background(Color(UIColor.systemBackground))
                .cornerRadius(8)
                .overlay(RoundedRectangle(cornerRadius: 8).stroke(Color.gray.opacity(0.2), lineWidth: 1))
            }
        }
    }
}

protocol HasLabel { var label: String { get } }
extension SupraregionalCuratorCompetition: HasLabel {}
extension InternationalNationalCompetition: HasLabel {}
extension ProvincialCuratorCompetition: HasLabel {}
extension SupraregionalProvincialCompetition: HasLabel {}
extension OtherCompetition: HasLabel {}

struct ResultView: View {
    let points: Float
    let onDismiss: () -> Void

    var body: some View {
        ZStack {
            Color(UIColor.systemBackground).ignoresSafeArea()
            VStack(spacing: 24) {
                Text("Twój wynik to:").font(.title2)
                Text(String(format: "%.1f", points)).font(.system(size: 80, weight: .bold)).foregroundColor(.accentColor)
                Text("punktów rekrutacyjnych").font(.headline).foregroundColor(.secondary)
                Spacer().frame(height: 60)
                Button(action: onDismiss) {
                    Text("POWRÓT").fontWeight(.bold).padding(.horizontal, 60).padding(.vertical, 16).background(Color.accentColor).foregroundColor(.white).cornerRadius(30)
                }
            }
            .padding()
        }
    }
}

// MARK: - Extensions for KMP Enums

extension SupraregionalCuratorCompetition {
    static func allCases() -> [SupraregionalCuratorCompetition] {
        return [.none, .subjectFinalist, .thematicOrInterdisciplinaryLaureate, .thematicOrInterdisciplinaryFinalist]
    }
}

extension InternationalNationalCompetition {
    static func allCases() -> [InternationalNationalCompetition] {
        return [.none, .artSubjectFinalist, .artTournamentLaureate, .artTournamentFinalist]
    }
}

extension ProvincialCuratorCompetition {
    static func allCases() -> [ProvincialCuratorCompetition] {
        return [.none, .multipleSubjectFinalists, .multipleThematicLaureates, .multipleThematicFinalists, .subjectFinalist, .thematicLaureate, .thematicFinalist]
    }
}

extension SupraregionalProvincialCompetition {
    static func allCases() -> [SupraregionalProvincialCompetition] {
        return [.none, .multipleSubjectFinalists, .multipleThematicLaureates, .multipleThematicFinalists, .subjectFinalist, .thematicLaureate, .thematicFinalist]
    }
}

extension OtherCompetition {
    static func allCases() -> [OtherCompetition] {
        return [.none, .international, .national, .provincial, .district]
    }
}

// MARK: - State Management

class CalculatorObservable: ObservableObject {
    let viewModel: CalcViewModel
    @Published var state: CalcState

    init(viewModel: CalcViewModel) {
        self.viewModel = viewModel
        self.state = viewModel.state.value as! CalcState

        Task { @MainActor in
            while true {
                let latest = viewModel.state.value as! CalcState
                if latest != self.state { self.state = latest }
                try? await Task.sleep(nanoseconds: 100 * 1_000_000)
            }
        }
    }
}
