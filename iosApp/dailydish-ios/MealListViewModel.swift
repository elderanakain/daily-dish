import Foundation
import DDCore
import KMPNativeCoroutinesAsync

@MainActor
public final class MealListViewModel: ObservableObject {
    
    @MainActor
    @Published
    var meals: [Meal] = []
    
    private let repository: MealRepository
    
    init(repository: MealRepository) {
        self.repository = repository
    }

    @MainActor
    func observeMeals() async{
        for try await meals in repository.observe() {
            self.meals = meals
        }
    }
}
