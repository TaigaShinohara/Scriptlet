import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("app.cash.paparazzi")
}

// Persistent signing key: the default debug keystore lives in the build
// machine's home directory and is silently regenerated whenever that machine
// is replaced, which changed the APK signature on every batch (updates then
// refuse to install, and reinstalled apps come back in the stopped state
// where widget providers cannot run). The key now lives in the project's
// .local/ (gitignored, never committed) so the signature is stable forever.
val signingPropsFile = rootProject.file(".local/signing.properties")
val signingProps = Properties().apply {
    if (signingPropsFile.exists()) signingPropsFile.inputStream().use { load(it) }
}
val hasPersistentSigning = signingPropsFile.exists() &&
    rootProject.file(".local/scriptlet.keystore").exists()

android {
    namespace = "app.scriptlet"
    compileSdk = 36
    buildToolsVersion = "35.0.0"

    defaultConfig {
        applicationId = "app.scriptlet"
        minSdk = 26
        targetSdk = 36
        versionCode = 44
        versionName = "0.28.0-feedback-motion"
    }

    signingConfigs {
        if (hasPersistentSigning) {
            create("persistent") {
                storeFile = rootProject.file(".local/scriptlet.keystore")
                storePassword = signingProps.getProperty("storePassword")
                keyAlias = signingProps.getProperty("keyAlias")
                keyPassword = signingProps.getProperty("keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            if (hasPersistentSigning) signingConfig = signingConfigs.getByName("persistent")
        }
        release {
            isMinifyEnabled = false
            if (hasPersistentSigning) signingConfig = signingConfigs.getByName("persistent")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")
    // Phase 2: home-screen widget via Glance
    implementation("androidx.glance:glance-appwidget:1.1.1")
    // 1.0.14 is the newest line whose AAR accepts compileSdk 36 (1.0.16+ needs 37)
    implementation("io.github.dokar3:quickjs-kt-android:1.0.14")
    // JVM screenshot tests (see app/src/test): renders the real Compose
    // screens off-device so layout collapse / clipping is caught before
    // delivery. Goldens live in app/src/test/snapshots.
    testImplementation("app.cash.paparazzi:paparazzi:1.3.5")
}
