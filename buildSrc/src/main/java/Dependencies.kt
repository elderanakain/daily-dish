object Versions {
  const val kotlin = "1.4.21"
  const val kotlinCoroutines = "1.4.2-native-mt"
  const val ktor = "1.5.0"
  const val kotlinxSerialization = "1.0.1"
  const val sqlDelight = "1.4.3"
  const val kotlinxDatetime = "0.1.1"

  const val sqliteJdbcDriver = "1.4.2"
  const val slf4j = "1.7.30"

  const val kotlinLogging = "2.0.4"

  const val koin = "3.0.0-alpha-4"
}

object Android {
  const val min = 28
  const val compile = 30
  const val target = compile
}

object Test {
  const val junit = "junit:junit:4.13"
}

object Ktor {
  const val clientCore = "io.ktor:ktor-client-core:${Versions.ktor}"
  const val clientJson = "io.ktor:ktor-client-json:${Versions.ktor}"
  const val clientLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
  const val clientSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"

  const val clientAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
  const val clientApache = "io.ktor:ktor-client-apache:${Versions.ktor}"
  const val slf4j = "org.slf4j:slf4j-api:${Versions.slf4j}"
}

object Serialization {
  const val core = "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerialization}"
}

object SqlDelight {
  const val runtime = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
  const val coroutineExtensions = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"

  const val androidDriver = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
  const val jdbcDriver = "com.squareup.sqldelight:jdbc-driver:${Versions.sqliteJdbcDriver}"

  const val connectionPooling = "com.zaxxer:HikariCP:3.4.5"
}

object DateTime {
  const val common = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDatetime}"
}

object Logging {
  const val common = "io.github.microutils:kotlin-logging:${Versions.kotlinLogging}"
}

object Coroutines {
  const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}"
  const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinCoroutines}"
}

object Koin {
  const val core = "org.koin:koin-core:${Versions.koin}"
  const val extended = "org.koin:koin-core-ext:${Versions.koin}"

  const val android = "org.koin:koin-android:${Versions.koin}"
  const val androidScope = "org.koin:koin-androidx-scope:${Versions.koin}"
  const val androidViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"

  const val ktor = "org.koin:koin-ktor:${Versions.koin}"
}
