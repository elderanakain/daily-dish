// swift-tools-version: 5.8
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/elderanakain/daily-dish/io/krugosvet/dailydish/common-kmmbridge/1.4.3/common-kmmbridge-1.4.3.zip"
let remoteKotlinChecksum = "9abd2672e7b42ef71e7e1341907ba9debcce4bb8c57cb7c3b284cd01f0963922"
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
