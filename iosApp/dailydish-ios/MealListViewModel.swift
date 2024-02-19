import Foundation
import DDCore
import OSLog

@MainActor
public final class MealListViewModel: ObservableObject {
    
    private let logger = Logger.init()
    
    @MainActor
    @Published
    var meals: [Meal] = []
    
    private let repository: MealRepository
    
    init(repository: MealRepository) {
        self.repository = repository
    }

    @MainActor
    func observeMeals() async throws {
        logger.debug("Fetching meals")
        
        do {
            try await repository.fetch()
        } catch {
            logger.error("\(error)")
        }
        
        logger.debug("Observing meals")
        
        for try await meals in repository.observe() {
            self.meals = meals
        }
    }
}
