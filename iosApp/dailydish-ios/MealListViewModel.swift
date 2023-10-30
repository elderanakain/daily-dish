import Foundation
import DDCore

public final class MealListViewModel: ObservableObject {
    
    @Published
    private (set) var meals: [Meal] = []
    
    private let repository: MealRepository = env!.mealRepository
    
    @MainActor
    func observeMeals() async {
        for await meals in repository.observe() {
            self.meals = meals
        }
    }
}
