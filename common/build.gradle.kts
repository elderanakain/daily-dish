import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
  id("com.android.library")
  kotlin("multiplatform")
  id("kotlinx-serialization")
  id("com.squareup.sqldelight")
  id("maven-publish")
  // https://www.marcogomiero.com/posts/2021/kmp-existing-project/
  // https://github.com/prof18/kmp-fatframework-cocoa
  id("com.prof18.kmp.fatframework.cocoa") version "0.0.1"
}

version = "1.0.9"
group = "io.krugosvet.dailydish"

kotlin {
  android {
    publishLibraryVariants("release", "debug")
    publishLibraryVariantsGroupedByFlavor = true
  }
  jvm()

  val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget =
    if (System.getenv("SDK_NAME")?.startsWith("iphoneos") == true) ::iosArm64 else ::iosX64

  iosTarget("ios") {
    binaries.framework(project.name)
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
    minSdkVersion(Android.min)
    targetSdkVersion(Android.target)
    versionCode = 1
    versionName = project.version as String

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

fatFrameworkCocoaConfig {
  fatFrameworkName = project.name
  namePrefix = project.name
  outputPath = "/Users/arubailo/Documents/daily-dish-framework"
  versionName = version.toString()

  cocoaPodRepoInfo {
    summary = "Provide Daily Dish common code"
    homepage = "https://github.com/elderanakain/daily-dish-framework"
    gitUrl = "git@github.com:elderanakain/daily-dish-framework.git"
  }
}