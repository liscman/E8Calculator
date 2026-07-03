import SwiftUI

/**
 * View representing the Settings screen on iOS.
 * Presented as a full-screen cover with a navigation bar to allow returning to the main view.
 */
struct SettingsView: View {
    /// Environment variable used to dismiss the current view
    @Environment(\.dismiss) var dismiss

    var body: some View {
        NavigationStack {
            VStack {
                Text("Settings Content")
                    .font(.title)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
            .navigationTitle("Settings")
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                // Leading button to close the settings and return to the main app
                ToolbarItem(placement: .topBarLeading) {
                    Button(action: { dismiss() }) {
                        Image(systemName: "arrow.left")
                            .accessibilityLabel("Back")
                    }
                }
            }
        }
    }
}
