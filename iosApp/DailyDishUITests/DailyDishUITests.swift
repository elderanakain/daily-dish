import XCTest

final class DailyDishUITests: XCTestCase {

    override func setUpWithError() throws {
        continueAfterFailure = false
    }

    override func tearDownWithError() throws {
        
    }

    func testTitleExists() throws {
        // when
        
        let app = XCUIApplication()
        app.launch()
        
        // then
        
        XCTAssertTrue(app.staticTexts["title"].exists)
    }
}
