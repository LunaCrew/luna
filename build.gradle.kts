plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
}

task("installGitHook", Copy::class) {
    from(File(rootProject.rootDir, "./tools/hooks/"))
    into(File(rootProject.rootDir, ".git/hooks"))
    filePermissions {
        user.execute = true
    }
}

tasks.getByPath(":app:preBuild").dependsOn(tasks.getByName("installGitHook"))
