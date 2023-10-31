import Foundation
import DDCore
import KMPNativeCoroutinesAsync

@MainActor
public final class MealListViewModel: ObservableObject {
    
    @Published
    var meals: [Meal] = []
    
    private let repository: MealRepository = AppDelegate.env.mealRepository
    
    @MainActor
    func observeMeals() async{
        do {
            for try await meals in asyncSequence(for: MealRepositoryNativeKt.observe(repository)) {
                self.meals = meals
            }
        } catch {
            print("Unexpected error: \(error).")
        }
    }
}