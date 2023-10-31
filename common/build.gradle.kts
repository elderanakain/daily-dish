import co.touchlab.skie.configuration.DefaultArgumentInterop
import co.touchlab.skie.configuration.EnumInterop
import co.touchlab.skie.configuration.FlowInterop
import co.touchlab.skie.configuration.SealedInterop
import co.touchlab.skie.configuration.SuspendInterop
import org.gradle.api.internal.artifacts.repositories.resolver.MavenUniqueSnapshotComponentIdentifier
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.mpp.BitcodeEmbeddingMode.*
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kmmBridge)
    alias(libs.plugins.skie)
    alias(libs.plugins.ksp)
    alias(libs.plugins.nativeCoroutines)

    id("maven-publish")
}

val isOnMaster: Boolean = (rootProject.extra.get("isOnMaster") as String).toBooleanStrict()
val framework = "DDCore"

version = libs.versions.common.get()
group = "io.krugosvet.dailydish"

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget {
        publishAllLibraryVariants()
    }

    jvm()

    listOf(iosArm64(), iosX64(), iosSimulatorArm64())
        .forEach {
            it.binaries {
                framework(framework) {
                    isStatic = true
                    embedBitcodeMode = DISABLE
                    binaryOption("bundleShortVersionString", version.toString())
                }
            }
        }

    explicitApiWarning()

    sourceSets {
        val commonMain by getting { dependencies { implementation(libs.bundles.common) } }
        val commonTest by getting { dependencies { implementation(kotlin("test")) } }
        val jvmMain by getting { dependencies { implementation(libs.bundles.jvm) } }
        val mobileMain by creating { dependsOn(commonMain) }

        val iosMain by getting {
            dependsOn(mobileMain)
            dependencies { implementation(libs.bundles.native) }
        }

        val androidMain by getting {
            dependsOn(mobileMain)
            dependencies { implementation(libs.bundles.android) }
        }

        all { languageSettings.optIn("kotlin.experimental.ExperimentalObjCName") }
    }
}

android {
    namespace = "io.krugosvet.dailydish"
    compileSdk = 33

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}

sqldelight {
    linkSqlite = true
    databases {
        create("Database") {
            packageName.set("io.krugosvet.dailydish.common.repository.db")
        }
    }
}

val publishRepository = "GitHubPackages"

publishing {
    repositories {
        maven {
            name = publishRepository
            url = uri("https://maven.pkg.github.com/elderanakain/daily-dish")
            credentials {
                username = System.getenv("DD_GH_USERNAME")
                password = System.getenv("DD_GG_TOKEN")
            }
        }
    }
}

kmmbridge {
    frameworkName = framework
    buildType = if (isOnMaster) NativeBuildType.RELEASE else NativeBuildType.DEBUG

    mavenPublishArtifacts(repository = publishRepository)
    spm(useCustomPackageFile = true)
    manualVersions()

    rootProject.extensions.extraProperties["spmBuildTargets"] = when {
        isOnMaster -> "ios_simulator_arm64,ios_arm64,ios_x64"
        else -> "ios_simulator_arm64"
    }

    val version = version.toString()

    if (!version.contains("-SNAPSHOT")) {
        return@kmmbridge
    }

    afterEvaluate {
        val updatePackageSwift by tasks.existing {
            outputs.upToDateWhen { false }

            doFirst {
                val snapShotVersion = project.dependencies
                    .create(project.group.toString(), "${project.name}-kmmbridge", version)
                    .let { configurations.detachedConfiguration(it) }
                    .apply { resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.MINUTES) }
                    .resolvedConfiguration.resolvedArtifacts.firstOrNull()
                    ?.id?.componentIdentifier?.let { it as? MavenUniqueSnapshotComponentIdentifier }
                    ?.timestamp
                    ?: error("Cannot resolve component timestamp")

                with(file("${layout.buildDirectory.get()}/faktory/url")) {
                    readText()
                        .replace("SNAPSHOT.zip", "$snapShotVersion.zip")
                        .let(::writeText)
                }
            }
        }
    }
}

skie {
    analytics {
        enabled = false
    }

    features {
        group {
            SuspendInterop.Enabled(true)
            FlowInterop.Enabled(true)
            EnumInterop.Enabled(true)
            SealedInterop.Enabled(true)

            DefaultArgumentInterop.Enabled(false)
        }
    }

    debug {
        printSkiePerformanceLogs = true
        crashOnSoftErrors = true
    }
}

nativeCoroutines {
    //exposedSeverity = ExposedSeverity.ERROR
}
