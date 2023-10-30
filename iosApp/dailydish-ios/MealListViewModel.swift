import Foundation
import DDCore

@MainActor
public final class MealListViewModel: ObservableObject {
    
    @Published
    var meals: [Meal] = []
    
    private let repository: MealRepository = AppDelegate.env.mealRepository
    
    @MainActor
    func observeMeals() async{
        do {
           
            try await repository.fetch()
            
            let meal = try await repository.get(mealId: "a30d89b5-bcd3-4f96-b750-e8ab3d09e79e")
            
            try await repository.add(meal: Meal(id: "id", title: "title", description: "description", lastCookingDate: DateTimExtensionsKt.currentDate))
            
            self.meals = [meal]
        } catch {
            print("Unexpected error: \(error).")
        }
    }
}
