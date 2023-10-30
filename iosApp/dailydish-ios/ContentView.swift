import SwiftUI
import DDCore

struct ContentView: View {

    var body: some View {
        let viewModel = MealListViewModel()
        
        VStack {
            Text("Meals").padding()
            
            List(viewModel.meals, id: \.id) { meal in
                Text(meal.title)
            }
            .task {
                await viewModel.observeMeals()
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
