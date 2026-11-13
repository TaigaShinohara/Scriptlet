package app.scriptlet.ui

import android.os.Build
import android.provider.Settings
import android.view.HapticFeedbackConstants
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.scriptlet.engine.RunResult
import app.scriptlet.engine.SampleScripts
import app.scriptlet.engine.ScriptEngine
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Brand colours come from the shared tokens (docs/ART_DIRECTION.md) so the
// onboarding can never drift from the rest of the app.
private val BrandPink = Tokens.Pink
private val BrandOrange = Tokens.Orange
private val BrandPurple = Tokens.Purple
private val BrandSky = Tokens.Sky

private const val PAGE_COUNT = 13
private const val PAGE_WORRY = 1
private const val PAGE_MATCH = 2
private const val PAGE_TRIAL = 5
private const val PAGE_TRIAL_RESULT = 6
private const val PAGE_BUILD = 10

/** The worry/wish picked on the question page; drives the match page, the value order, the trial sample and the preselected build choice. */
private data class Worry(
    val label: String,
    val mirror: String,
    val strength: String,
    val trioOrder: List<Int>,
    val trialKey: String,
    val buildKey: String?,
)

private val worries = listOf(
    Worry(
        label = "今週の予定を、パッと見たい",
        mirror = "「今週の予定を、パッと見たい」",
        strength = "月間カレンダーの完成品サンプルがあるから、置くだけで今月が一覧できます。",
        trioOrder = listOf(1, 0, 2),
        trialKey = "weekly-calendar",
        buildKey = "weekly-calendar",
    ),
    Worry(
        label = "天気や数字を、すぐチェックしたい",
        mirror = "「天気や数字を、すぐチェックしたい」",
        strength = "天気も進捗も、本物のデータを取って表示するサンプルが揃っています。",
        trioOrder = listOf(0, 2, 1),
        trialKey = "progress-demo",
        buildKey = "showcase-weather",
    ),
    Worry(
        label = "好きな見た目に、とことん飾りたい",
        mirror = "「好きな見た目に、とことん飾りたい」",
        strength = "色も文字も配置も、スクリプトで全部変えられます。",
        trioOrder = listOf(2, 0, 1),
        trialKey = "stylish-clock",
        buildKey = "showcase-countdown",
    ),
    Worry(
        label = "まだわからない",
        mirror = "",
        strength = "決めなくて大丈夫。本物のサンプルが動くところから、見てみましょう。",
        trioOrder = listOf(0, 1, 2),
        trialKey = "hello",
        buildKey = null,
    ),
)

/** A first-run build choice: which sample the editor opens with after onboarding. */
private data class BuildChoice(val label: String, val sampleKey: String?)

private val buildChoices = listOf(
    BuildChoice("🌤 天気ウィジェット", "showcase-weather"),
    BuildChoice("🗓 月間カレンダー", "weekly-calendar"),
    BuildChoice("⏳ カウントダウン", "showcase-countdown"),
    BuildChoice("✍️ まっさらから書く", null),
)

/** Praise lines for the medium/large tiers: 3+ patterns each, never the same twice in a row, one sentence, specific. */
private class PraiseBook {
    private val last = mutableMapOf<String, Int>()

    fun next(pool: String, patterns: List<String>): String {
        var idx = patterns.indices.random()
        if (patterns.size > 1 && idx == last[pool]) idx = (idx + 1) % patterns.size
        last[pool] = idx
        return patterns[idx]
    }
}

private val worryPraise = listOf(
    "教えてくれてありがとう。その使い方、いちばん分かりやすいよ",
    "なるほど。その希望、Scriptletの得意分野だよ",
    "いいね。その場面に合うサンプルが、ちゃんとあるよ",
)

private val trialPraise = listOf(
    "動いた！それ、本物の実行結果だよ",
    "やった、その場で本物が動いたね",
    "成功！プレビューの仕組み、そのままだよ",
)

private val finishPraise = listOf(
    "準備はばっちり。あとは作るだけ！",
    "最高のスタートだね。いってらっしゃい！",
    "さあ、本番。最初の1個を作ろう！",
)

/** OS-standard haptics only (never raw vibration), so device settings are respected. */
@Composable
private fun rememberHaptics(): (Int) -> Unit {
    val view = LocalView.current
    return remember(view) {
        { tier: Int ->
            runCatching {
                when (tier) {
                    1 -> view.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
                    else -> if (Build.VERSION.SDK_INT >= 30) {
                        view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)
                    } else {
                        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
                    }
                }
            }
        }
    }
}

@Composable
private fun rememberReducedMotion(): Boolean {
    val context = LocalContext.current
    return remember {
        runCatching {
            Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1f,
            ) == 0f
        }.getOrDefault(false)
    }
}

/** Praise bounce (or a fade when reduced motion is on); starts immediately, never blocks input. */
@Composable
private fun PraiseMotion(trigger: Int, reducedMotion: Boolean, content: @Composable () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = if (trigger % 2 == 0) 1f else 1.04f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "praiseScale",
    )
    val alpha by animateFloatAsState(
        targetValue = if (trigger % 2 == 0) 1f else 0.75f,
        label = "praiseAlpha",
    )
    Box(
        modifier = Modifier.graphicsLayer {
            if (reducedMotion) {
                this.alpha = alpha
            } else {
                scaleX = scale
                scaleY = scale
            }
        },
    ) { content() }
}

/**
 * Ambient layer: at most two soft shapes drifting a few pixels on slow,
 * unsynchronised cycles — the page's air, never competing with the hero.
 * Stopped entirely under reduced motion.
 */
@Composable
private fun AmbientBackground(reducedMotion: Boolean, active: Boolean) {
    if (reducedMotion || !active) return
    val transition = rememberInfiniteTransition(label = "ambient")
    val driftA by transition.animateFloat(
        initialValue = -4f, targetValue = 4f,
        animationSpec = infiniteRepeatable(tween(2900), RepeatMode.Reverse),
        label = "driftA",
    )
    val driftB by transition.animateFloat(
        initialValue = 3f, targetValue = -3f,
        animationSpec = infiniteRepeatable(tween(3700), RepeatMode.Reverse),
        label = "driftB",
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .offset(x = driftA.dp, y = driftB.dp)
                .align(Alignment.TopEnd)
                .padding(top = 70.dp, end = 26.dp)
                .size(54.dp)
                .clip(CircleShape)
                .background(BrandPink.copy(alpha = 0.10f)),
        )
        Box(
            modifier = Modifier
                .offset(x = driftB.dp, y = driftA.dp)
                .align(Alignment.BottomStart)
                .padding(start = 22.dp, bottom = 150.dp)
                .size(38.dp)
                .clip(CircleShape)
                .background(BrandSky.copy(alpha = 0.10f)),
        )
    }
}

/** Hero layer: a gentle float that keeps the page's hero alive and draws the eye to it. Static under reduced motion. */
@Composable
private fun HeroFloat(reducedMotion: Boolean, active: Boolean, content: @Composable () -> Unit) {
    if (reducedMotion || !active) {
        Box { content() }
        return
    }
    val transition = rememberInfiniteTransition(label = "heroFloat")
    val lift by transition.animateFloat(
        initialValue = -3f, targetValue = 3f,
        animationSpec = infiniteRepeatable(tween(2600), RepeatMode.Reverse),
        label = "lift",
    )
    Box(modifier = Modifier.offset(y = lift.dp)) { content() }
}

/** Entrance for result pages: the reaction's afterglow carries into the page (fade + small rise). */
@Composable
private fun ResultEntrance(reducedMotion: Boolean, content: @Composable () -> Unit) {
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { shown = true }
    val alpha by animateFloatAsState(if (shown) 1f else 0f, tween(280), label = "resultAlpha")
    val rise by animateFloatAsState(if (shown) 0f else if (reducedMotion) 0f else 14f, tween(280), label = "resultRise")
    Box(modifier = Modifier.graphicsLayer { this.alpha = alpha; translationY = rise }) { content() }
}

/** A slow pulse for the one element a page wants noticed (frame, arrow, button). */
@Composable
private fun PulseAlpha(reducedMotion: Boolean, content: @Composable (Float) -> Unit) {
    if (reducedMotion) {
        content(1f)
        return
    }
    val transition = rememberInfiniteTransition(label = "pulse")
    val a by transition.animateFloat(
        initialValue = 0.45f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1150), RepeatMode.Reverse),
        label = "pulseAlpha",
    )
    content(a)
}

/**
 * First-launch onboarding (mobile_onboarding skill v0.5): one claim per page;
 * questions and their results always live on separate pages; question pages
 * have no Next button — choosing reacts (praise trio) and auto-advances.
 * Every page carries two animation layers: a hero motion with a meaning and
 * a quiet ambient motion (max 2 elements, stopped under reduced motion).
 */
@Composable
fun OnboardingScreen(
    engine: ScriptEngine,
    onFinish: (name: String, code: String, desc: String) -> Unit,
    onSkip: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { PAGE_COUNT })
    val scope = rememberCoroutineScope()
    val haptics = rememberHaptics()
    val reducedMotion = rememberReducedMotion()
    val praise = remember { PraiseBook() }
    var worry by remember { mutableStateOf<Worry?>(null) }
    var build by remember { mutableStateOf<BuildChoice?>(null) }
    var trialResult by remember { mutableStateOf<RunResult?>(null) }
    var trialPraiseLine by remember { mutableStateOf<String?>(null) }
    // Set once the user leaves via skip/list: a celebration that is still
    // counting down must not yank them into the editor afterwards.
    var exited by remember { mutableStateOf(false) }

    // Navigation runs as a replaceable job, never behind a boolean guard: a
    // guard flag whose reset is skipped when an animated scroll is
    // interrupted (user swipe) stays on forever and silently kills every
    // later navigation — buttons look dead. Cancelling and relaunching is
    // self-healing by construction.
    var navJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }
    fun goTo(page: Int) {
        navJob?.cancel()
        navJob = scope.launch { pagerState.animateScrollToPage(page) }
    }

    /** Auto-advance after a choice/run: go, then verify once and retry if the pager never moved. */
    fun goToVerified(fromPage: Int, toPage: Int) {
        scope.launch {
            goTo(toPage)
            delay(700)
            if (!exited && pagerState.currentPage == fromPage) goTo(toPage)
        }
    }

    fun leaveToList() {
        exited = true
        onSkip()
    }

    fun finish() {
        if (exited) return
        val key = build?.sampleKey
        val idx = if (key != null) SampleScripts.keys.indexOf(key) else -1
        if (idx >= 0) {
            val (name, code) = SampleScripts.all[idx]
            onFinish(name, code, SampleScripts.descs.getOrElse(idx) { "" })
        } else {
            onFinish("新しいスクリプト", "// ここにスクリプトを書く\nconst widget = new ListWidget();\n", "")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AmbientBackground(reducedMotion, active = true)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(20.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                // Skip stays neutral: no praise, no success haptic.
                TextButton(onClick = { leaveToList() }) { Text("スキップ") }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth().weight(1f),
            ) { page ->
                val active = page == pagerState.currentPage
                when (page) {
                    0 -> PageShell(
                        headline = "ホーム画面を、自分の道具に",
                        sub = "毎日見るその画面、もっと自分らしくできます。",
                        reducedMotion = reducedMotion, active = active,
                        hero = { HomeMock(variant = 0, reducedMotion = reducedMotion) },
                    )
                    PAGE_WORRY -> WorryPage(
                        praise = praise, haptics = haptics, reducedMotion = reducedMotion,
                        onChoose = { w ->
                            worry = w
                            scope.launch {
                                delay(350)
                                goToVerified(PAGE_WORRY, PAGE_MATCH)
                            }
                        },
                    )
                    PAGE_MATCH -> ResultEntrance(reducedMotion) { MatchPage(worry, reducedMotion, active) }
                    3 -> PageShell(
                        headline = "書くと、ウィジェットになる",
                        sub = "JavaScriptを書く。それだけでホーム画面の表示が作れます。",
                        reducedMotion = reducedMotion, active = active,
                        hero = { CodeToWidgetMock(reducedMotion) },
                    )
                    4 -> PageShell(
                        headline = "中身は、なんでも自由",
                        sub = "天気、予定、進捗…。表示したいものを、表示できます。",
                        reducedMotion = reducedMotion, active = active,
                        hero = { TrioMock(worry?.trioOrder ?: listOf(0, 1, 2)) },
                    )
                    PAGE_TRIAL -> TrialPage(
                        engine = engine, worry = worry, praise = praise,
                        haptics = haptics, reducedMotion = reducedMotion,
                        onDone = { result, line ->
                            trialResult = result
                            trialPraiseLine = line
                            scope.launch {
                                delay(350)
                                goToVerified(PAGE_TRIAL, PAGE_TRIAL_RESULT)
                            }
                        },
                    )
                    PAGE_TRIAL_RESULT -> ResultEntrance(reducedMotion) {
                        TrialResultPage(trialResult, trialPraiseLine)
                    }
                    7 -> PageShell(
                        stepLabel = "ステップ 1 / 3",
                        headline = "サンプルから選ぶ",
                        sub = "完成品のサンプルが9本。まずは選ぶだけで大丈夫。",
                        reducedMotion = reducedMotion, active = active,
                        hero = { SampleListMock(reducedMotion) },
                    )
                    8 -> PageShell(
                        stepLabel = "ステップ 2 / 3",
                        headline = "実行して確かめる",
                        sub = "▶ 実行プレビューで、見え方がその場でわかります。",
                        reducedMotion = reducedMotion, active = active,
                        hero = { PreviewMock(reducedMotion) },
                    )
                    9 -> PageShell(
                        stepLabel = "ステップ 3 / 3",
                        headline = "ホーム画面に置く",
                        sub = "「ホーム画面に追加」を押せば、もう完成です。",
                        reducedMotion = reducedMotion, active = active,
                        hero = { HomeMock(variant = 1, reducedMotion = reducedMotion) },
                    )
                    PAGE_BUILD -> BuildPage(
                        worry = worry, haptics = haptics, reducedMotion = reducedMotion,
                        onChoose = { c ->
                            build = c
                            scope.launch {
                                delay(320)
                                goToVerified(PAGE_BUILD, 11)
                            }
                        },
                    )
                    11 -> PageShell(
                        headline = "毎日見る画面が、変わる",
                        sub = "時計も天気も予定も、全部が自分の手作りになります。",
                        reducedMotion = reducedMotion, active = active,
                        hero = { HomeMock(variant = 2, reducedMotion = reducedMotion) },
                    )
                    else -> FinishPage(build, praise, haptics, reducedMotion, onStart = { finish() }, onList = { leaveToList() })
                }
            }
            // Window dots: at most 5 visible, sliding with the page, edges
            // smaller, the total never countable.
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val current = pagerState.currentPage
                val windowSize = 5
                val start = (current - 2).coerceIn(0, maxOf(0, PAGE_COUNT - windowSize))
                val end = minOf(PAGE_COUNT, start + windowSize)
                for (i in start until end) {
                    val isEdge = (i == start && start > 0) || (i == end - 1 && end < PAGE_COUNT)
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(
                                when {
                                    i == current -> 10.dp
                                    isEdge -> 4.dp
                                    else -> 7.dp
                                },
                            )
                            .clip(CircleShape)
                            .background(if (i == current) BrandPink else MaterialTheme.colorScheme.outlineVariant),
                    )
                }
            }
            // Question pages and the trial page have no Next button: the
            // choice / the run itself is the way forward.
            val noNext = pagerState.currentPage in listOf(PAGE_WORRY, PAGE_TRIAL, PAGE_BUILD)
            if (pagerState.currentPage < PAGE_COUNT - 1 && !noNext) {
                Button(
                    onClick = {
                        haptics(1)
                        goTo(pagerState.currentPage + 1)
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text("つぎへ") }
            }
        }
    }
}

@Composable
private fun PageShell(
    headline: String,
    sub: String,
    stepLabel: String? = null,
    reducedMotion: Boolean,
    active: Boolean,
    hero: @Composable () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            HeroFloat(reducedMotion, active) { hero() }
        }
        if (stepLabel != null) {
            Text(stepLabel, style = MaterialTheme.typography.labelLarge, color = BrandPink, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
        }
        Text(
            headline,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            sub,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
    }
}

/**
 * Question page (worry/wish). Only the question and the choices live here;
 * pressing one fires the medium praise trio and auto-advances to the match
 * page — the result is never written on this page.
 */
@Composable
private fun WorryPage(
    praise: PraiseBook,
    haptics: (Int) -> Unit,
    reducedMotion: Boolean,
    onChoose: (Worry) -> Unit,
) {
    var selected by remember { mutableStateOf<Worry?>(null) }
    var reaction by remember { mutableStateOf<String?>(null) }
    var bounce by remember { mutableIntStateOf(0) }
    // Safety net: choosing disables the cards while the auto-advance runs.
    // If the advance ever fails and this page is still up 1.6s later, free
    // the choices again — a question page must never become a dead end.
    LaunchedEffect(selected) {
        if (selected != null) {
            delay(1600)
            selected = null
            reaction = null
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            "どんなホーム画面にしたい？",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "いちばん近いものを、1つ選んでください。",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        worries.forEach { w ->
            val isSelected = selected == w
            PraiseMotion(trigger = if (isSelected) bounce else 0, reducedMotion = reducedMotion) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                        .clickable(enabled = selected == null) {
                            selected = w
                            reaction = praise.next("worry", worryPraise)
                            bounce++
                            haptics(2)
                            onChoose(w)
                        },
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, BrandPink) else null,
                    colors = if (isSelected) {
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    } else {
                        CardDefaults.cardColors()
                    },
                ) {
                    Text(
                        (if (isSelected) "✓ " else "") + w.label,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        // Only the momentary reaction lives here; the result is on the next page.
        Text(
            reaction ?: "",
            style = MaterialTheme.typography.bodyLarge,
            color = BrandPink,
            textAlign = TextAlign.Center,
        )
    }
}

/** Result page for the worry question: the match declaration alone, the user's own words mirrored, one strength. */
@Composable
private fun MatchPage(worry: Worry?, reducedMotion: Boolean, active: Boolean) {
    val w = worry
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            HeroFloat(reducedMotion, active) { HomeMock(variant = 1, reducedMotion = reducedMotion) }
        }
        if (w != null && w.mirror.isNotEmpty()) {
            Text(
                "そんなあなたには、Scriptletがぴったり",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "${w.mirror} —— その希望、得意分野です。\n${w.strength}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        } else {
            Text(
                "まずは、動くところから",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                w?.strength ?: "決めなくて大丈夫。本物のサンプルが動くところから、見てみましょう。",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
        Spacer(Modifier.height(16.dp))
    }
}

/**
 * Trial page: only the operation lives here. The outcome is handed to the
 * parent and shown on the separate result page.
 */
@Composable
private fun TrialPage(
    engine: ScriptEngine,
    worry: Worry?,
    praise: PraiseBook,
    haptics: (Int) -> Unit,
    reducedMotion: Boolean,
    onDone: (RunResult, String?) -> Unit,
) {
    var running by remember { mutableStateOf(false) }
    var bounce by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    val sampleIdx = SampleScripts.keys.indexOf(worry?.trialKey ?: "hello").coerceAtLeast(0)
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "「${SampleScripts.all[sampleIdx].first}」を、実際に動かしてみます",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
                Spacer(Modifier.height(18.dp))
                PraiseMotion(trigger = bounce, reducedMotion = reducedMotion) {
                    Button(
                        enabled = !running,
                        onClick = {
                            running = true
                            scope.launch {
                                // Always land on the result page with *some*
                                // outcome — a stalled or failed run must never
                                // leave this button dead.
                                val r = try {
                                    kotlinx.coroutines.withTimeoutOrNull(20_000) {
                                        val (_, code) = SampleScripts.all[sampleIdx]
                                        engine.run("onboarding-trial", code)
                                    } ?: RunResult.Error(
                                        "実行が20秒以内に終わりませんでした。もう一度試してください。",
                                        null, emptyList(),
                                    )
                                } catch (e: Exception) {
                                    RunResult.Error("実行に失敗しました: ${e.message}", null, emptyList())
                                } finally {
                                    running = false
                                }
                                if (r is RunResult.Success) {
                                    bounce++
                                    haptics(2)
                                    onDone(r, praise.next("trial", trialPraise))
                                } else {
                                    onDone(r, null)
                                }
                            }
                        },
                    ) { Text(if (running) "実行中…" else "▶ 実行してみる") }
                }
            }
        }
        Text(
            "本物が、その場で動く",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "お試しではなく、本物のサンプルが本物の仕組みで動きます。",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
    }
}

/** Result page for the trial: the rendered preview (or the error) and the praise line live here, not on the trial page. */
@Composable
private fun TrialResultPage(result: RunResult?, praiseLine: String?) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            when (val r = result) {
                is RunResult.Success -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        WidgetPreview(r.tree, Modifier.fillMaxWidth().padding(8.dp))
                    }
                    if (praiseLine != null) {
                        Spacer(Modifier.height(14.dp))
                        Text(praiseLine, style = MaterialTheme.typography.bodyLarge, color = BrandPink, textAlign = TextAlign.Center)
                    }
                }
                is RunResult.Error -> Text(
                    "エラー: ${r.message}",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
                null -> Text(
                    "まだ実行していません。スワイプで戻って、▶ 実行してみるを押してね。",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Text(
            "これが、実行結果",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(16.dp))
    }
}

/** Question page 2 (build choice): no Next button and no result text here; choosing reacts small and auto-advances. */
@Composable
private fun BuildPage(
    worry: Worry?,
    haptics: (Int) -> Unit,
    reducedMotion: Boolean,
    onChoose: (BuildChoice) -> Unit,
) {
    var selected by remember { mutableStateOf<BuildChoice?>(null) }
    var bounce by remember { mutableIntStateOf(0) }
    // Preselect from the worry answer (a starting state, not a decision):
    // the page still advances only when the user presses a choice.
    LaunchedEffect(worry) {
        if (selected == null && worry?.buildKey != null) {
            selected = buildChoices.firstOrNull { it.sampleKey == worry.buildKey }
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            "最初に作りたいのは？",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "選んだサンプルで、最後にそのまま作り始められます。",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        buildChoices.forEach { c ->
            val isSelected = selected == c
            PraiseMotion(trigger = if (isSelected) bounce else 0, reducedMotion = reducedMotion) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
                        .clickable {
                            selected = c
                            bounce++
                            haptics(1)
                            onChoose(c)
                        },
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, BrandPink) else null,
                    colors = if (isSelected) {
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    } else {
                        CardDefaults.cardColors()
                    },
                ) {
                    Text(
                        (if (isSelected) "✓ " else "") + c.label,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp),
                    )
                }
            }
        }
    }
}

/** Connection page: the large-tier celebration fires once, then the editor opens. */
@Composable
private fun FinishPage(
    build: BuildChoice?,
    praise: PraiseBook,
    haptics: (Int) -> Unit,
    reducedMotion: Boolean,
    onStart: () -> Unit,
    onList: () -> Unit,
) {
    var celebrating by remember { mutableStateOf<String?>(null) }
    var bounce by remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            PraiseMotion(trigger = bounce, reducedMotion = reducedMotion) {
                HomeMock(variant = 1, reducedMotion = reducedMotion)
            }
        }
        Text(
            celebrating ?: "最初のウィジェットを作ろう",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (celebrating != null) BrandPink else MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            if (build != null) {
                "「${build.label}」の編集画面が開きます。▶ 実行プレビューですぐ見られます。"
            } else {
                "サンプルの編集画面が開きます。▶ 実行プレビューですぐ見られます。"
            },
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = {
                if (celebrating == null) {
                    celebrating = praise.next("finish", finishPraise)
                    bounce++
                    haptics(3)
                    scope.launch {
                        delay(650)
                        onStart()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) { Text("作ってみる") }
        Spacer(Modifier.height(6.dp))
        OutlinedButton(onClick = onList, modifier = Modifier.fillMaxWidth()) { Text("一覧から始める") }
        Spacer(Modifier.height(16.dp))
    }
}

// ---- Hero visuals (pure Compose shapes; one hero per page) ----

@Composable
private fun MiniWidgetCard(title: String, body: String, accent: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Tokens.SlateDark),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(accent))
            Spacer(Modifier.height(6.dp))
            Text(title, color = Color.White, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
            Text(body, color = Tokens.MockTextSub, style = MaterialTheme.typography.labelSmall)
        }
    }
}

/**
 * A phone-ish home screen mock. variant: 0 = one widget + a pulsing empty
 * slot (the hero motion: "yours goes here"), 1 = placed widgets, 2 = full.
 */
@Composable
private fun HomeMock(variant: Int, reducedMotion: Boolean) {
    Box(
        modifier = Modifier
            .width(230.dp)
            .height(300.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(Tokens.SlateFrame)
            .border(3.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(28.dp))
            .padding(14.dp),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            MiniWidgetCard("9:41", "おはようございます", BrandPink, Modifier.fillMaxWidth())
            if (variant == 0) {
                PulseAlpha(reducedMotion) { a ->
                    Box(
                        modifier = Modifier.fillMaxWidth().height(64.dp).clip(RoundedCornerShape(16.dp))
                            .border(2.dp, BrandOrange.copy(alpha = a), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("＋ ここに作れる", color = BrandOrange.copy(alpha = a), style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    MiniWidgetCard("🌤 24°", "大阪・晴れ", BrandSky, Modifier.weight(1f))
                    MiniWidgetCard("⏳ 48日", "年末まで", BrandOrange, Modifier.weight(1f))
                }
            }
            if (variant == 2) {
                MiniWidgetCard("🗓 今週", "月 火 水 木 金", BrandPurple, Modifier.fillMaxWidth())
                MiniWidgetCard("達成率 72%", "■■■■□□", BrandPink, Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun CodeToWidgetMock(reducedMotion: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Card(modifier = Modifier.weight(1.2f), colors = CardDefaults.cardColors(containerColor = Tokens.SlateDark)) {
            Text(
                "const w =\n  new ListWidget();\nw.addText(\"Hello!\");",
                color = Tokens.MockSky,
                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                modifier = Modifier.padding(12.dp),
            )
        }
        // Hero motion: the arrow nudges right — code becomes a widget.
        if (reducedMotion) {
            Text("→", style = MaterialTheme.typography.headlineMedium, color = BrandPink, fontWeight = FontWeight.Bold)
        } else {
            val transition = rememberInfiniteTransition(label = "arrow")
            val nudge by transition.animateFloat(
                initialValue = 0f, targetValue = 3f,
                animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
                label = "nudge",
            )
            Text(
                "→",
                style = MaterialTheme.typography.headlineMedium,
                color = BrandPink,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.offset(x = nudge.dp),
            )
        }
        MiniWidgetCard("Hello!", "Scriptlet", BrandPink, Modifier.weight(1f))
    }
}

@Composable
private fun TrioMock(order: List<Int>) {
    val cards = listOf(
        Triple("🌤 天気", "24° 晴れ", BrandSky),
        Triple("🗓 予定", "水：乗馬", BrandPurple),
        Triple("📈 進捗", "72%", BrandOrange),
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        order.forEach { i ->
            val (title, body, accent) = cards[i]
            MiniWidgetCard(title, body, accent, Modifier.weight(1f))
        }
    }
}

@Composable
private fun SampleListMock(reducedMotion: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        listOf(
            "🌤 大阪の天気（実データ）" to "天気と気温グラフ",
            "⏳ 年末カウントダウン" to "残り日数を円ゲージで",
            "🗓 週間ダッシュボード" to "予定とタスクのまとめ",
        ).forEachIndexed { i, (name, desc) ->
            // Hero motion: the recommended first card's frame gently pulses.
            if (i == 0) {
                PulseAlpha(reducedMotion) { a ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = androidx.compose.foundation.BorderStroke(2.dp, BrandPink.copy(alpha = a)),
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(name, style = MaterialTheme.typography.titleSmall)
                            Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            } else {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(name, style = MaterialTheme.typography.titleSmall)
                        Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
private fun PreviewMock(reducedMotion: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
        MiniWidgetCard("Hello, Scriptlet!", "いま 9:41", BrandPink, Modifier.fillMaxWidth())
        Spacer(Modifier.height(14.dp))
        // Hero motion: the run button pulses — "press me" without a word.
        PulseAlpha(reducedMotion) { a ->
            Box(
                modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(BrandPink.copy(alpha = 0.55f + 0.45f * a)).padding(horizontal = 22.dp, vertical = 10.dp),
            ) {
                Text("▶ 実行プレビュー", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
