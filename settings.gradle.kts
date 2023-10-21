pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

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
}


rootProject.name = "daily-dish"

include(":common", ":androidApp", "backEnd")
