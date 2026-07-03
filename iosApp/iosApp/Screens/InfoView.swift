import SwiftUI

/**
 * View representing the Info screen on iOS.
 * Currently serves as a placeholder for application information and documentation.
 */
struct InfoView: View {
    var body: some View {
        VStack {
            Text("InfoScreen")
                .font(.largeTitle)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(UIColor.systemBackground))
    }
}
