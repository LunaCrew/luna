// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt) apply false
}

task("installGitHook", Copy::class) {
    from(File(rootProject.rootDir, "./tools/hooks/"))
    into(File(rootProject.rootDir, ".git/hooks"))
    fileMode = 777
}

tasks.getByPath(":app:preBuild").dependsOn(tasks.getByName("installGitHook"))
