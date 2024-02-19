import SwiftUI
import DDCore
import Foundation
import os

struct ContentView: View {
    
    private let logger = Logger.init()
    
    @ObservedObject var viewModel = MealListViewModel(repository: DailyDishApp.env.mealRepository)

    var body: some View {
        MealList(meals: viewModel.meals)
            .task {
                do {
                    try await viewModel.observeMeals()
                } catch {
                    logger.error("\(error)")
                }
            }
    }
}

struct MealList: View {
    
    var meals: [Meal]
    
    var body: some View {
        VStack {
            Text("Meals")
                .accessibilityLabel("title")
                .padding()
            
            List(meals, id: \.id) { meal in
                MealRow(meal: meal)
                    .padding()
            }
        }
    }
}

struct MealRow: View {
    
    var meal: DDCore.Meal
    
    var body: some View {
        Text("\(meal.title)")
    }
}

#Preview {
    let meals = [
        Meal(
            id: "id",
            title: "Best-Ever French Dip",
            description: "description",
            lastCookingDate: DateTimExtensionsKt.currentDate
        ),
        Meal(
            id: "id1",
            title: "Best Fried Fish Tacos",
            description: "description",
            lastCookingDate: DateTimExtensionsKt.currentDate
        ),
    ]
    
    return MealList(meals: meals)
}
