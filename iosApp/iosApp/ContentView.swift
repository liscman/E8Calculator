import SwiftUI
import SharedLogic

struct ContentView: View {
    @State private var showContent = false
    var body: some View {
        VStack {
            Text("Click me!")
           
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
