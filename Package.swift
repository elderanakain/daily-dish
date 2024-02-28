// swift-tools-version: 5.8
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/elderanakain/daily-dish/io/krugosvet/dailydish/common-kmmbridge/1.4.2-SNAPSHOT/common-kmmbridge-1.4.2-20240228.210416-8.zip"
let remoteKotlinChecksum = "de48de665b198208f44738429fb31c8d236894579e7c8104ef55970f632ff69c"
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
