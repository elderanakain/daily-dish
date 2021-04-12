plugins {
  id("com.android.library")
  kotlin("multiplatform")
  id("kotlinx-serialization")
  id("com.squareup.sqldelight")
  id("maven-publish")
}

version = "1.0.8"
group = "io.krugosvet.dailydish"

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

kotlin {
  android {
    publishLibraryVariants("release", "debug")
    publishLibraryVariantsGroupedByFlavor = true
  }
  jvm()

  // Revert to just ios() when gradle plugin can properly resolve it
  val onPhone = System.getenv("SDK_NAME")?.startsWith("iphoneos") ?: false
  if (onPhone) {
    iosArm64("ios")
  } else {
    iosX64("ios")
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
