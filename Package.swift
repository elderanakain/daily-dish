// swift-tools-version: 5.8
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/elderanakain/daily-dish/io/krugosvet/dailydish/common-kmmbridge/1.3.0-SNAPSHOT/common-kmmbridge-1.3.0-20231101.204357-48.zip"
let remoteKotlinChecksum = "f057be6a7b276484c3eb892e8b02d14e35e4c52f0d0b4ba785185489d169058b"
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
