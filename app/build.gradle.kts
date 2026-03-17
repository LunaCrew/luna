import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.sentry)
}

android {
    namespace = "lunacrew.luna"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "lunacrew.luna"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "0.1-alpha-a"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val keyName = "SUPABASE_PUBLISHABLE_KEY"
        val urlName = "SUPABASE_URL"
        val supabaseConfig = getSupabaseConfig(urlName, keyName)
        val sentryDsn = getSentryConfig("DSN")

        buildConfigField("String", keyName, "\"${supabaseConfig.first}\"")
        buildConfigField("String", urlName, "\"${supabaseConfig.second}\"")
        buildConfigField("String", "SENTRY_DSN", "\"$sentryDsn\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.adaptive.navigation.suite)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.google.fonts)
    implementation(libs.androidx.compose.constraintlayout)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.hilt)
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.auth)
    implementation(libs.supabase.realtime)
    implementation(libs.supabase.storage)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.logging)
    implementation(libs.slf4j)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.reorderable)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.emoji)
    implementation(libs.zip4j)
    ksp(libs.room.compiler)
    ksp(libs.hilt.compiler)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

sentry {
    org.set("luna-ks")
    projectName.set("luna-android")
    includeSourceContext.set(true)
    authToken.set(getSentryConfig("AUTH_TOKEN"))
}

fun getSupabaseConfig(urlName: String, keyName: String): Pair<String, String> {
    val properties = Properties()

    if (File("supabase.properties").exists()) {
        properties.load(rootProject.file("supabase.properties").inputStream())
        val url = properties.getProperty(urlName)
        val key = properties.getProperty(keyName)
        return Pair(url, key)
    } else {
        val url = System.getenv(urlName) ?: ""
        val key = System.getenv(keyName) ?: ""
        return Pair(url, key)
    }
}

fun getSentryConfig(param: String): String {
    val properties = Properties()

    if (File("sentry.properties").exists()) {
        properties.load(rootProject.file("sentry.properties").inputStream())
        return properties.getProperty(param)
    } else {
        return System.getenv(param) ?: ""
    }
}
