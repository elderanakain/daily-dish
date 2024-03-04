// swift-tools-version: 5.8
import PackageDescription

// BEGIN KMMBRIDGE VARIABLES BLOCK (do not edit)
let remoteKotlinUrl = "https://maven.pkg.github.com/elderanakain/daily-dish/io/krugosvet/dailydish/common-kmmbridge/1.4.3-SNAPSHOT/common-kmmbridge-1.4.3-20240304.205514-2.zip"
let remoteKotlinChecksum = "4c997e7cfbb22a2bdb275b172b87ee9393ba4e589bbcbb2979dd4fc9a647a850"
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
