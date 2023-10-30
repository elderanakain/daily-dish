import SwiftUI
import DDCore

var env: DDCore.Environment? = nil

@main
struct dailydish_iosApp: App {
    
    var body: some Scene {
        return WindowGroup {
            ContentView()
        }
    }
    
    init() {
        env = EnvironmentCompanion.shared.doInit()
    }
}
