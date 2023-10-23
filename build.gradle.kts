import org.jmailen.gradle.kotlinter.tasks.LintTask

plugins {
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.android.app) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotliner) apply false
}

subprojects {
    apply(plugin = "org.jmailen.kotlinter")

    tasks.withType<LintTask>().configureEach {
        exclude { it.file.path.contains("/build/generated/") }
    }

    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(1, TimeUnit.MINUTES)
    }
}
