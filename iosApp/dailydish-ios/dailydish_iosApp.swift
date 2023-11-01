import SwiftUI
import DDCore

@main
struct DailyDishApp: App {
    
    @UIApplicationDelegateAdaptor private var appDelegate: AppDelegate
    
    var body: some Scene {
        return WindowGroup {
            ContentView()
        }
    }
}
