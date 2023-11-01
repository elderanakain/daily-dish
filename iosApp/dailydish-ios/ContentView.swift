import SwiftUI
import DDCore
import Foundation

struct ContentView: View {
    
    @ObservedObject var viewModel = MealListViewModel(repository: AppDelegate.env.mealRepository)

    var body: some View {
        VStack {
            Text("Meals")
                .accessibilityLabel("title")
                .padding()
            
            List(viewModel.meals, id: \.id) { meal in
                MealRow(meal: meal)
            }
            .task {
                await viewModel.observeMeals()
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

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
