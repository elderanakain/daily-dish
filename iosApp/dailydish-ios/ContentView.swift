import SwiftUI
import common

struct ContentView: View {
    var repository = koin.mealRepository
    
    @State var mealsText = String("")
    
    var body: some View {
        return Text(mealsText)
            .padding()
            .onAppear(perform: {
                repository.fetch { (unit, error) -> Void in
                    mealsText = repository.meals.map { $0.title }.joined()
                }
            })
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
