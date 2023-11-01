import Foundation
import DDCore
import KMPNativeCoroutinesAsync

@MainActor
public final class MealListViewModel: ObservableObject {
    
    @MainActor
    @Published
    var meals: [Meal] = []
    
    private let repository: MealRepository = AppDelegate.env.mealRepository
    
    @MainActor
    func observeMeals() async{
        for try await meals in repository.observe() {
            self.meals = meals
        }
    }
}
