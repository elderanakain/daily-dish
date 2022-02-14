plugins {
    id("com.android.library")
    kotlin("multiplatform")
    id("kotlinx-serialization")
    id("com.squareup.sqldelight")
    id("maven-publish")
    id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
}

version = "1.0.11"
group = "io.krugosvet.dailydish"

kotlin {
    android {
        publishLibraryVariants("release", "debug")
        publishLibraryVariantsGroupedByFlavor = true
    }
    jvm()
    ios {
        binaries.framework {
            baseName = "common"
        }
    }

    explicitApiWarning()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0-native-mt")

                implementation("io.ktor:ktor-client-core:1.6.7")
                implementation("io.ktor:ktor-client-json:1.6.7")
                implementation("io.ktor:ktor-client-logging:1.6.7")
                implementation("io.ktor:ktor-client-serialization:1.6.7")

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.0")

                implementation("com.squareup.sqldelight:runtime:1.5.3")
                implementation("com.squareup.sqldelight:coroutines-extensions:1.5.3")

                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.0")

                implementation("io.insert-koin:koin-core:3.1.5")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }

        // Backend
        val jvmMain by getting {
            dependencies {
                implementation( "io.ktor:ktor-client-apache:1.6.4")
                api("org.slf4j:slf4j-api:1.7.32")

                implementation("com.squareup.sqldelight:jdbc-driver:1.5.3")
                implementation("com.zaxxer:HikariCP:5.0.1")
            }
        }

        // Mobile
        val mobileMain by creating {
            dependsOn(commonMain)
        }

        val iosMain by getting {
            dependsOn(mobileMain)

            dependencies {
                implementation("io.ktor:ktor-client-ios:1.6.4")

                implementation("com.squareup.sqldelight:native-driver:1.5.3")
            }
        }

        val androidMain by getting {
            sourceSets.apply {
                kotlin.setSrcDirs(listOf("src/androidMain/kotlin"))
                resources.setSrcDirs(listOf("src/androidMain/resources"))
            }

            dependsOn(mobileMain)

            dependencies {
                implementation("io.ktor:ktor-client-android:1.6.4")
                api("org.slf4j:slf4j-api:1.7.32")

                implementation("com.squareup.sqldelight:android-driver:1.5.3")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0-native-mt")
                implementation("io.insert-koin:koin-android:3.1.2")
            }
        }
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 28
        targetSdk = 31
    }
}

sqldelight {
    database("Database") {
        packageName = "io.krugosvet.dailydish.common.repository.db"
        sourceFolders = listOf("sqldelight")
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/elderanakain/daily-dish-common")
            credentials {
                username = System.getenv("GITHUB_PUBLISH_USERNAME")
                password = System.getenv("GITHUB_PUBLISH_TOKEN")
            }
        }
    }
}

multiplatformSwiftPackage {
    packageName("common")
    swiftToolsVersion("5.4")
    targetPlatforms {
        iOS { v("13") }
    }
    distributionMode { local() }
    outputDirectory(File("$projectDir/../../daily-dish-package", "/"))
}
