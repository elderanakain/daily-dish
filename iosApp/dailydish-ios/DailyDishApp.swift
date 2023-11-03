import SwiftUI
import DDCore

@main
struct DailyDishApp: App {
    
    static let env: DDCore.Environment = DDCore.EnvironmentCompanion.shared.doInit()
    
    var body: some Scene {
        return WindowGroup {
            ContentView()
        }
    }
}
