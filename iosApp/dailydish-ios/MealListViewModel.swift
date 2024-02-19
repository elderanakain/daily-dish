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
        
        Task {
            do {
                try await repository.fetch()
            } catch {
                logger.error("\(error)")
            }
        }
    }

    @MainActor
    func observeMeals() async {
        logger.debug("Observing meals")
        
        do {
            for try await meals in repository.observe() {
                self.meals = meals
            }
        } catch {
            logger.error("\(error)")
        }
    }
}
