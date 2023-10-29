import SwiftUI
import DDCore

@main
struct dailydish_iosApp: App {
    
    private let env: DDCore.Environment
    
    var body: some Scene {
        return WindowGroup {
            ContentView(repository: env.mealRepository)
        }
    }
    
    init() {
        env = EnvironmentCompanion.shared.doInit()
    }
}
