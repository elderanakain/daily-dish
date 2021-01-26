buildscript {
  repositories {
    google()
    mavenCentral()
    jcenter()
  }
  dependencies {
    classpath(Android.gradlePlugin)
    classpath(Kotlin.gradlePlugin)
    classpath(Serialization.gradlePlugin)
    classpath(SqlDelight.gradlePlugin)
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
    jcenter()
    maven(url = "https://dl.bintray.com/ekito/koin")
    maven(url = "https://kotlin.bintray.com/kotlin-js-wrappers/")
    maven(url = "https://jitpack.io")
    maven(url = "https://kotlin.bintray.com/kotlinx/")
  }
}
