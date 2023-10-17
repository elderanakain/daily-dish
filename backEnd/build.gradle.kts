plugins {
    kotlin("jvm")
    alias(libs.plugins.serialization)
    `maven-publish`
}

group = "io.krugosvet.dailydish"
version = "0.3.0"

dependencies {
    implementation(libs.bundles.backEnd)
    testImplementation(libs.bundles.backEnd.test)
}
