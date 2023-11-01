import Foundation
import UIKit
import DDCore

class AppDelegate: NSObject, UIApplicationDelegate {
    
    static let env: DDCore.Environment = DDCore.EnvironmentCompanion.shared.doInit()

    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        return true
    }
}
