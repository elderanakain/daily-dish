import io.ktor.plugin.features.DockerImageRegistry
import io.ktor.plugin.features.DockerPortMapping
import kotlinx.kover.gradle.plugin.dsl.MetricType

plugins {
    kotlin("jvm")
    alias(libs.plugins.serialization)
    alias(libs.plugins.ktor)
    `maven-publish`
    alias(libs.plugins.kover)
}

group = "io.krugosvet.dailydish"
version = libs.versions.common.get()

dependencies {
    implementation(libs.bundles.backEnd)
    testImplementation(libs.bundles.backEnd.test)
}

ktor {
    docker {
        localImageName = "dd-be"
        jreVersion = JavaVersion.VERSION_17
        portMappings = (listOf(DockerPortMapping(9080, 9080)))
        externalRegistry = DockerImageRegistry.externalRegistry(
            username = provider { System.getenv("DD_GH_USERNAME") },
            password = provider { System.getenv("DD_GG_TOKEN") },
            project = provider { "DailyDish" },
            namespace = provider { "https://ghcr.io" },
        )
    }
}

jib.from {
    platforms {
        platform {
            architecture = "arm64"
            os = "linux"
        }
    }
}

koverReport {
    defaults {
        log { coverageUnits = MetricType.INSTRUCTION }
    }

    verify {
        rule { minBound(90, metric = MetricType.INSTRUCTION) }
    }
}
