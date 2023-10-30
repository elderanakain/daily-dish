import Foundation
import DDCore

public final class MealListViewModel: ObservableObject {
    
    @Published
    private (set) var meals: [Meal] = []
    
    private let repository: MealRepository = AppDelegate.env.mealRepository
    
    
    func observeMeals() {
        Task {
            
            do {
                for try await meals in repository.observe() {
                    //self.meals = meals
                }
            } catch {
                print("Unexpected error: \(error).")
            }
        }
    }
}
