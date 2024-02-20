pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

val isOnMaster: Boolean by extra { providers.gradleProperty("isOnMaster").get().toBoolean() }

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()

        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/elderanakain/daily-dish")
            credentials {
                username = System.getenv("DD_GH_USERNAME")
                password = System.getenv("DD_GG_TOKEN")
            }
        }
    }

    versionCatalogs {
        create("libs") {
            version("common", "1.4.2".let { if (!isOnMaster) it.plus("-SNAPSHOT") else it })
        }
    }
}


rootProject.name = "daily-dish"

include(":common", ":androidApp", "backEnd")
