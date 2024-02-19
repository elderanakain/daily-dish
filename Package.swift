// swift-tools-version: 5.8
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/elderanakain/daily-dish/io/krugosvet/dailydish/common-kmmbridge/1.4.1-SNAPSHOT/common-kmmbridge-1.4.1-20240219.232242-5.zip"
let remoteKotlinChecksum = "d37a77f2d5396bac5d4542c5cc0be72efd82ed1c7afb09a2a05e0ff8536b0c74"
let packageName = "DDCore"
// END KMMBRIDGE BLOCK

let package = Package(
    name: packageName,
    platforms: [
        .iOS(.v15)
    ],
    products: [
        .library(
            name: packageName,
            targets: [packageName]
        ),
    ],
    targets: [
        .binaryTarget(
            name: packageName,
            url: remoteKotlinUrl,
            checksum: remoteKotlinChecksum
        ),
    ]
)
