plugins {
  id("com.android.library")
  kotlin("multiplatform")
  id("kotlinx-serialization")
  id("com.squareup.sqldelight")
  id("maven-publish")
  id("com.chromaticnoise.multiplatform-swiftpackage") version "2.0.3"
}

version = "1.0.10"
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
        implementation(Coroutines.core)

        implementation(Ktor.clientCore)
        implementation(Ktor.clientJson)
        implementation(Ktor.clientLogging)
        implementation(Ktor.clientSerialization)

        implementation(Serialization.core)

        implementation(SqlDelight.runtime)
        implementation(SqlDelight.coroutineExtensions)

        implementation(DateTime.common)

        implementation(Koin.core)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
        implementation(Test.junit)
      }
    }

    // Backend
    val jvmMain by getting {
      dependencies {
        implementation(Ktor.clientApache)
        api(Ktor.slf4j)

        implementation(SqlDelight.jdbcDriver)
        implementation(SqlDelight.connectionPooling)
      }
    }

    // Mobile
    val mobileMain by creating {
      dependsOn(commonMain)
    }

    val iosMain by getting {
      dependsOn(mobileMain)

      dependencies {
        implementation(Ktor.clientIOS)

        implementation(SqlDelight.nativeDriver)
      }
    }

    val androidMain by getting {
      sourceSets.apply {
        kotlin.setSrcDirs(listOf("src/androidMain/kotlin"))
        resources.setSrcDirs(listOf("src/androidMain/resources"))
      }

      dependsOn(mobileMain)

      dependencies {
        implementation(Ktor.clientAndroid)
        api(Ktor.slf4j)

        implementation(SqlDelight.androidDriver)
        implementation(Coroutines.android)
        implementation(Koin.android)
      }
    }
  }
}

android {
  compileSdkVersion(Android.compile)
  sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
  defaultConfig {
    minSdk = Android.min
    targetSdk = Android.target

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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
