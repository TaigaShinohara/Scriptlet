plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.4.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.0" apply false
    // JVM screenshot tests (pre-delivery UI verification, no emulator):
    // Paparazzi renders the Compose screens with layoutlib on the JVM.
    id("app.cash.paparazzi") version "1.3.5" apply false
}
