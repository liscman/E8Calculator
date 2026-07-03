import SwiftUI

/**
 * The main view of the iOS application.
 * Manages the top-level navigation, including a swipeable TabView for main sections,
 * a custom bottom navigation bar, and the logic for presenting the Settings screen.
 */
struct ContentView: View {
    /// Currently selected tab index (0: Calculator, 1: Calendar, 2: Info)
    @State private var selectedTab = 0
    /// State controlling the visibility of the full-screen settings cover
    @State private var isSettingsOpen = false

    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                // Content with Paging (Swipeable)
                // TabView with .page style allows users to swipe horizontally between screens
                TabView(selection: $selectedTab) {
                    CalculatorView()
                        .tag(0)
                    CalendarView()
                        .tag(1)
                    InfoView()
                        .tag(2)
                }
                .tabViewStyle(.page(indexDisplayMode: .never))

                Divider()

                // Bottom Navigation Bar
                // Custom implementation to match the Material 3 feel while maintaining iOS aesthetics.
                // Each button takes equal width (maxWidth: .infinity) for perfect symmetry.
                HStack(spacing: 0) {
                    TabButton(title: "Calculator", icon: "plus.slash.minus", index: 0, selection: $selectedTab)
                    TabButton(title: "Calendar", icon: "calendar", index: 1, selection: $selectedTab)
                    TabButton(title: "Info", icon: "info.circle", index: 2, selection: $selectedTab)
                }
                .padding(.vertical, 10)
                .background(Color(UIColor.secondarySystemBackground))
            }
            .navigationTitle(titleForSelection(selectedTab))
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button(action: { isSettingsOpen = true }) {
                        Image(systemName: "gearshape")
                            .accessibilityLabel("Settings")
                    }
                }
            }
            .fullScreenCover(isPresented: $isSettingsOpen) {
                SettingsView()
            }
        }
    }

    /**
     * Returns the appropriate title for the navigation bar based on the current selection.
     * - Parameter selection: The index of the selected tab.
     * - Returns: A localized string title.
     */
    private func titleForSelection(_ selection: Int) -> String {
        switch selection {
        case 0: return "Calculator"
        case 1: return "Calendar"
        case 2: return "Info"
        default: return ""
        }
    }
}

/**
 * A reusable button for the bottom navigation bar.
 * Updates the bound selection and animates the transition.
 */
struct TabButton: View {
    let title: String
    let icon: String
    let index: Int
    @Binding var selection: Int

    var body: some View {
        Button(action: {
            withAnimation {
                selection = index
            }
        }) {
            VStack(spacing: 4) {
                Image(systemName: icon)
                    .font(.system(size: 22))
                Text(title)
                    .font(.caption2)
            }
            .frame(maxWidth: .infinity)
            .foregroundColor(selection == index ? .accentColor : .secondary)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
