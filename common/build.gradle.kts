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

  buildTypes {
    release {
      isMinifyEnabled = false
      setMatchingFallbacks(listOf("release"))
    }

    debug {
      isDebuggable = true
      setMatchingFallbacks(listOf("release"))
    }
  }

  // workaround for https://youtrack.jetbrains.com/issue/KT-43944
  configurations {
    create("androidTestApi")
    create("androidTestDebugApi")
    create("androidTestReleaseApi")
    create("testApi")
    create("testDebugApi")
    create("testReleaseApi")
  }
}

kotlin {
  explicitApiWarning()

  android {
    publishLibraryVariants("release", "debug")
    publishLibraryVariantsGroupedByFlavor = true
  }
  jvm()

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

        implementation(Logging.common)

        implementation(Koin.core)
        implementation(Koin.extended)
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test-junit"))
        implementation(Test.junit)
      }
    }

    val androidMain by getting {
      dependencies {
        implementation(Ktor.clientAndroid)
        api(Ktor.slf4j)

        implementation(SqlDelight.androidDriver)
        implementation(Coroutines.android)
        implementation(Koin.android)
      }
    }

    listOf(
      "androidAndroidTest", "androidAndroidTestDebug", "androidAndroidTestRelease", "androidDebug", "androidRelease",
      "androidTest", "androidTestDebug", "androidTestRelease"
    ).forEach {
      sourceSets[it].apply {
        dependsOn(androidMain)

        kotlin.setSrcDirs(listOf("src/$it/kotlin"))
        resources.setSrcDirs(listOf("src/$it/resources"))
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
