import Foundation
import DDCore

func startKoin() {
    _koin = KoinKt.doInit().koin
}

private var _koin: Koin_coreKoin? = nil
var koin: Koin_coreKoin {
    return _koin!
}
