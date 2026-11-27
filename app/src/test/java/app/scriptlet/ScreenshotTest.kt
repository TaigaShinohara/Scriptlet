package app.scriptlet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import app.scriptlet.data.ScriptStore
import app.scriptlet.engine.ScriptEngine
import app.scriptlet.ui.OnboardingScreen
import app.scriptlet.ui.ScriptletTheme
import org.junit.Rule
import org.junit.Test

/**
 * JVM screenshot tests (pre-delivery UI verification — this project is
 * built on a VM with no emulator, so screens are verified on the JVM
 * before delivery; see AGENTS.md "Pre-delivery UI verification standard").
 *
 * Coverage, per the standard:
 *  1. First screen after launch: the script list, seeded by a real
 *     [ScriptStore] (first launch seeds exactly the two templates).
 *  2. Onboarding entry: page 1, the hook.
 *  3. A screen rebuilt in the latest release: the editor (v0.20.0 noise
 *     audit touched list / editor / help / widget-config).
 *
 * No script is ever executed here: QuickJS is native and the editor only
 * runs the engine on demand (button press), which a snapshot never does.
 * The engine instance below is constructed but never asked to run.
 *
 * How to run (this VM cannot use Gradle's test worker — its loopback
 * channel is intercepted and the worker dies before discovery):
 *   .local/run-screenshot-tests.sh record   # regenerate goldens
 *   .local/run-screenshot-tests.sh verify   # check against goldens
 * Full procedure and the classpath recipe: docs/UI_VERIFICATION.md.
 */
class ScreenshotTest {

    companion object {
        /**
         * Paparazzi copies android.os.Build fields from layoutlib's
         * _Original_Build, whose FINGERPRINT (and a few siblings) are null
         * in the generic layoutlib build. Compose's PrefetchScheduler
         * static init calls toLowerCase() on FINGERPRINT and NPEs, which
         * kills any screen using a lazy layout. Fill the nulls with
         * harmless values before anything composes.
         */
        @org.junit.BeforeClass
        @JvmStatic
        fun patchNullBuildFields() {
            val unsafeClass = Class.forName("sun.misc.Unsafe")
            val theUnsafe = unsafeClass.getDeclaredField("theUnsafe")
            theUnsafe.isAccessible = true
            val unsafe = theUnsafe.get(null)
            val putObject = unsafeClass.getMethod(
                "putObject", Any::class.java, Long::class.javaPrimitiveType, Any::class.java
            )
            val staticFieldBase = unsafeClass.getMethod("staticFieldBase", java.lang.reflect.Field::class.java)
            val staticFieldOffset = unsafeClass.getMethod("staticFieldOffset", java.lang.reflect.Field::class.java)
            for (name in listOf("FINGERPRINT", "BRAND", "MODEL", "DEVICE", "PRODUCT", "MANUFACTURER")) {
                val field = android.os.Build::class.java.getField(name)
                if (field.get(null) == null) {
                    putObject.invoke(
                        unsafe,
                        staticFieldBase.invoke(unsafe, field),
                        staticFieldOffset.invoke(unsafe, field),
                        "paparazzi"
                    )
                }
            }
        }
    }

    @get:Rule
    val paparazzi = Paparazzi(deviceConfig = DeviceConfig.PIXEL_5)

    /** A store seeded exactly as on first launch (the two templates). */
    private fun seededStore(): ScriptStore = ScriptStore(paparazzi.context)

    @Test
    fun listScreen_seededTemplates() {
        val store = seededStore()
        paparazzi.snapshot {
            ScriptletTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ListScreen(
                        store = store,
                        onOpen = {},
                        onNew = { _, _, _ -> },
                        onHelp = {},
                    )
                }
            }
        }
    }

    @Test
    fun onboarding_firstPage() {
        val store = seededStore()
        paparazzi.snapshot {
            ScriptletTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    OnboardingScreen(
                        engine = ScriptEngine(store),
                        onFinish = { _, _, _ -> },
                        onSkip = {},
                    )
                }
            }
        }
    }

    @Test
    fun feedScreen_presetCards() {
        val store = seededStore()
        paparazzi.snapshot {
            ScriptletTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    app.scriptlet.ui.FeedScreen(
                        store = store,
                        listState = androidx.compose.foundation.lazy.LazyListState(),
                        onOpenScript = {},
                    )
                }
            }
        }
    }

    @Test
    fun editorScreen_weatherTemplateLoaded() {
        val store = seededStore()
        val weather = store.scripts.value.firstOrNull { it.name.contains("天気") }
            ?: store.scripts.value.first()
        paparazzi.snapshot {
            ScriptletTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    EditorScreen(
                        store = store,
                        engine = ScriptEngine(store),
                        initial = weather,
                        onBack = {},
                    )
                }
            }
        }
    }
}
