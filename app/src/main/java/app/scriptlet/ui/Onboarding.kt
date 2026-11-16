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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.sp
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

private const val PAGE_COUNT = 12
private const val PAGE_WORRY = 1
private const val PAGE_MATCH = 2
private const val PAGE_TRIAL = 5
private const val PAGE_TRIAL_RESULT = 6

/** The worry/wish picked on the question page; drives the match page, the value order, the trial sample and the preselected build choice. */
private data class Worry(
    val label: String,
    val mirror: String,
    val strength: String,
    val trioOrder: List<Int>,
    val trialKey: String,
    val buildKey: String?,
)

// Exactly three choices (skill v0.8.0): the two strongest concrete wishes
// for this app — practical information at a glance, and decorating the home
// screen — plus the mandatory escape hatch. The two build targets are
// exactly the two bundled templates.
private val worries = listOf(
    Worry(
        label = "天気や予定を、パッと見たい",
        mirror = "「天気や予定を、パッと見たい」",
        strength = "天気もカレンダーも、本物のデータを取って表示する完成品サンプルが揃っています。",
        trioOrder = listOf(0, 1, 2),
        trialKey = "weekly-calendar",
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
        trialKey = "daily-art",
        buildKey = null,
    ),
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
 * Staged entrance for the match page (skill v0.8.0 ordering): blocks arrive
 * one after another — the mirrored words first, then the declaration, then
 * the reason — 160ms apart, never all at once. A fade replaces the rise
 * under reduced motion; the ORDER is kept either way.
 */
@Composable
private fun Staged(index: Int, reducedMotion: Boolean, content: @Composable () -> Unit) {
    var shown by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 160L)
        shown = true
    }
    val alpha by animateFloatAsState(if (shown) 1f else 0f, tween(260), label = "stagedAlpha")
    val rise by animateFloatAsState(
        if (shown) 0f else if (reducedMotion) 0f else 12f, tween(260), label = "stagedRise",
    )
    Box(modifier = Modifier.graphicsLayer { this.alpha = alpha; translationY = rise }) { content() }
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
 * First-launch onboarding (mobile_onboarding skill v0.8.0): one claim per
 * page; questions and their results always live on separate pages; question
 * pages have no Next button — choosing reacts on the spot (praise trio),
 * rests for a beat so the reaction can be read, and only then advances.
 * Animation exists only where it means something: the praise reactions, the
 * pulse on the one slot the hook page wants noticed, the staged entrance of
 * the match page. No ambient loops, no floating heroes.
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
        // No second question page (removed at the user's direction,
        // 2026-11-14 — "I want to use the app already"): the first question's
        // answer already carries the recommended first sample.
        val key = worry?.buildKey
        val idx = if (key != null) SampleScripts.keys.indexOf(key) else -1
        if (idx >= 0) {
            val (name, code) = SampleScripts.all[idx]
            onFinish(name, code, SampleScripts.descs.getOrElse(idx) { "" })
        } else {
            onFinish("新しいスクリプト", "// ここにスクリプトを書く\nconst widget = new ListWidget();\n", "")
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                                // Reaction first, then a readable rest:
                                // the praise lands on THIS page for ~1s
                                // before the match page arrives (skill
                                // v0.8.0: never show both at once).
                                delay(1000)
                                goToVerified(PAGE_WORRY, PAGE_MATCH)
                            }
                        },
                    )
                    PAGE_MATCH -> MatchPage(worry, reducedMotion)
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
                                // Same ordering as the question page:
                                // the success reaction rests ~1s on the
                                // trial page before the result page.
                                delay(1000)
                                goToVerified(PAGE_TRIAL, PAGE_TRIAL_RESULT)
                            }
                        },
                        onLater = { leaveToList() },
                    )
                    PAGE_TRIAL_RESULT -> ResultEntrance(reducedMotion) {
                        TrialResultPage(trialResult, trialPraiseLine)
                    }
                    7 -> PageShell(
                        stepLabel = "ステップ 1 / 3",
                        headline = "サンプルから選ぶ",
                        sub = "完成品のサンプルが14本。まずは選ぶだけで大丈夫。",
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
                        hero = { PlaceMock() },
                    )
                    10 -> PageShell(
                        headline = "毎日見る画面が、変わる",
                        sub = "時計も天気も予定も、全部が自分の手作りになります。",
                        reducedMotion = reducedMotion, active = active,
                        hero = { HomeMock(variant = 2, reducedMotion = reducedMotion) },
                    )
                    else -> {
                        val idx = worry?.buildKey?.let { SampleScripts.keys.indexOf(it) } ?: -1
                        val sampleName = if (idx >= 0) SampleScripts.all[idx].first else null
                        FinishPage(sampleName, praise, haptics, reducedMotion, onStart = { finish() }, onList = { leaveToList() })
                    }
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
            val noNext = pagerState.currentPage in listOf(PAGE_WORRY, PAGE_TRIAL)
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
            hero()
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

/**
 * Result page for the worry question: the match declaration alone.
 * Entrance order (skill v0.8.0): the user's own words mirrored back FIRST,
 * then the declaration, then the one strength — staged, never simultaneous.
 */
@Composable
private fun MatchPage(worry: Worry?, reducedMotion: Boolean) {
    val w = worry
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(12.dp))
        Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
            HomeMock(variant = 1, reducedMotion = reducedMotion)
        }
        if (w != null && w.mirror.isNotEmpty()) {
            Staged(0, reducedMotion) {
                Text(
                    w.mirror,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = BrandPink,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(6.dp))
            Staged(1, reducedMotion) {
                Text(
                    "そんなあなたには、Scriptletがぴったり",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(8.dp))
            Staged(2, reducedMotion) {
                Text(
                    w.strength,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            Staged(0, reducedMotion) {
                Text(
                    "まずは、動くところから",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(Modifier.height(8.dp))
            Staged(1, reducedMotion) {
                Text(
                    w?.strength ?: "決めなくて大丈夫。本物のサンプルが動くところから、見てみましょう。",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
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
    onLater: () -> Unit,
) {
    var running by remember { mutableStateOf(false) }
    var bounce by remember { mutableIntStateOf(0) }
    // "Try it later" (skill v0.8.0): choosing it ENDS the onboarding as
    // completed — quietly, no celebration, no blame. The parent saves the
    // completion flag on this exit path exactly like a normal finish.
    var laterNote by remember { mutableStateOf(false) }
    LaunchedEffect(laterNote) {
        if (laterNote) {
            delay(900)
            onLater()
        }
    }
    val scope = rememberCoroutineScope()
    val sampleIdx = SampleScripts.keys.indexOf(worry?.trialKey ?: "daily-art").coerceAtLeast(0)
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
                                        // Detached: awaiting a child of this
                                        // scope would trap the timeout itself
                                        // if the run stalls in native code.
                                        engine.runDetached("onboarding-trial", code).await()
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
                    ) {
                        if (running) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("実行中…")
                        } else {
                            Text("▶ 実行してみる")
                        }
                    }
                }
                Spacer(Modifier.height(14.dp))
                if (laterNote) {
                    Text(
                        "あとで大丈夫。本体から、いつでも試せます",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                    )
                } else {
                    OutlinedButton(
                        enabled = !running,
                        onClick = {
                            haptics(1)
                            laterNote = true
                        },
                    ) { Text("あとで試す") }
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

/** Connection page: the large-tier celebration fires once, then the editor opens. */
@Composable
private fun FinishPage(
    sampleName: String?,
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
                StartMock(sampleName)
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
            if (sampleName != null) {
                "「$sampleName」の編集画面が開きます。▶ 実行プレビューですぐ見られます。"
            } else {
                "まっさらな編集画面が開きます。▶ 実行プレビューですぐ見られます。"
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
        // The arrow is static: a perpetual nudge loop would be decoration,
        // not meaning (skill v0.8.0). The direction reads fine standing still.
        Text("→", style = MaterialTheme.typography.headlineMedium, color = BrandPink, fontWeight = FontWeight.Bold)
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

/** A bar that stands in for a line of text in a schematic mock — no legible words, so a teaching page never reads as the real, tappable thing. */
@Composable
private fun SkeletonBar(widthDp: Int, heightDp: Int = 10, color: Color) {
    Box(
        modifier = Modifier.width(widthDp.dp).height(heightDp.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(color),
    )
}

@Composable
private fun SampleListMock(reducedMotion: Boolean) {
    // Schematic only (user direction, 2026-11-14): skeleton bars instead of
    // legible sample names, no pulse inviting a tap, and an explicit
    // "illustration" label. This page teaches the steps; the real list is
    // one swipe past the end of the onboarding. (The old mock also carried
    // stale sample names that no longer exist.)
    val barColor = MaterialTheme.colorScheme.onSurface
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "イラスト（手順の説明）",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End),
        )
        Spacer(Modifier.height(6.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val tints = listOf(BrandPink, BrandOrange, BrandSky)
            listOf(150, 128, 142).forEachIndexed { i, titleWidth ->
                val row: @Composable () -> Unit = {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier.size(34.dp).clip(CircleShape)
                                .background(tints[i].copy(alpha = 0.25f)),
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            SkeletonBar(titleWidth, color = barColor.copy(alpha = 0.30f))
                            SkeletonBar(titleWidth * 62 / 100, heightDp = 8, color = barColor.copy(alpha = 0.16f))
                        }
                    }
                }
                if (i == 0) {
                    // The chosen one: a still pink frame and tint — selected,
                    // but quiet. Nothing on a teaching page should pulse.
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = androidx.compose.foundation.BorderStroke(2.dp, BrandPink),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                        ),
                    ) { row() }
                } else {
                    Card(modifier = Modifier.fillMaxWidth()) { row() }
                }
            }
        }
    }
}

@Composable
private fun PreviewMock(reducedMotion: Boolean) {
    // Step 2 teaches the editor's core loop, so — unlike the other step
    // illustrations — the button here deliberately looks like the REAL
    // ▶ 実行プレビュー button (user direction, 2026-11-15: it must be
    // recognisable as the actual button, and the picture must show that
    // pressing it renders the preview right here, on the same page).
    // Everything else stays schematic, and the whole picture keeps its
    // イラスト label so it is not mistaken for a working screen.
    val barColor = MaterialTheme.colorScheme.onSurface
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "イラスト（手順の説明）",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End),
        )
        Spacer(Modifier.height(6.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    SkeletonBar(118, color = BrandPink.copy(alpha = 0.55f))
                    SkeletonBar(186, heightDp = 9, color = barColor.copy(alpha = 0.18f))
                    SkeletonBar(96, heightDp = 9, color = barColor.copy(alpha = 0.18f))
                }
            }
            Spacer(Modifier.height(12.dp))
            // The real button, drawn faithfully: pink pill, ▶ and its label.
            Box(
                modifier = Modifier.clip(RoundedCornerShape(999.dp)).background(BrandPink)
                    .padding(horizontal = 22.dp, vertical = 11.dp),
            ) {
                Text(
                    "▶ 実行プレビュー",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Text(
                "▼",
                color = BrandPink,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp),
            )
            // The preview appears right here, in the same page.
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(10.dp).clip(CircleShape).background(BrandOrange))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "プレビュー",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = BrandPink,
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    SkeletonBar(150, heightDp = 10, color = barColor.copy(alpha = 0.35f))
                    Spacer(Modifier.height(6.dp))
                    SkeletonBar(104, heightDp = 10, color = barColor.copy(alpha = 0.22f))
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "押すと、その場でここに表示されます",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

/** Step 3's illustration: the PLACING action — a widget card coming down
 *  into an empty slot on the home screen. (Until v0.16.2 this page reused
 *  the finished-home-screen picture, which made three pages in a row look
 *  identical; user report 2026-11-15.) */
@Composable
private fun PlaceMock() {
    val slotColor = MaterialTheme.colorScheme.onSurfaceVariant
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "イラスト（手順の説明）",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.End),
        )
        Spacer(Modifier.height(6.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
            ),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // App-icon-like squares: the rest of the home screen, abstracted.
                Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                    listOf(BrandPink, BrandSky, BrandPurple, BrandOrange).forEach { c ->
                        Box(
                            Modifier.size(26.dp).clip(RoundedCornerShape(8.dp))
                                .background(c.copy(alpha = 0.5f)),
                        )
                    }
                }
                Spacer(Modifier.height(18.dp))
                // The widget card, on its way down.
                Card(
                    modifier = Modifier.width(190.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF16283F)),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Box(Modifier.size(9.dp).clip(CircleShape).background(BrandPink))
                        Spacer(Modifier.height(7.dp))
                        SkeletonBar(96, heightDp = 10, color = Color.White.copy(alpha = 0.75f))
                        Spacer(Modifier.height(5.dp))
                        SkeletonBar(64, heightDp = 9, color = Color.White.copy(alpha = 0.4f))
                    }
                }
                Text(
                    "▼",
                    color = BrandPink,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 5.dp),
                )
                // The empty slot it lands in.
                Box(
                    modifier = Modifier.width(190.dp).height(58.dp)
                        .drawBehind {
                            drawRoundRect(
                                color = slotColor,
                                style = Stroke(
                                    width = 2.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 9f)),
                                ),
                                cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx()),
                            )
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "ここに置く",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

/** The finish page's hero: not the home screen (that is the expectation
 *  page's picture) but the very sample the user is about to open — the
 *  "this is what you make first" card. */
@Composable
private fun StartMock(sampleName: String?) {
    val emoji = if (sampleName != null) sampleName.substringBefore(' ') else "✍️"
    val title = if (sampleName != null) sampleName.substringAfter(' ', sampleName) else "まっさらなスクリプト"
    Card(
        modifier = Modifier.width(280.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(emoji, fontSize = 52.sp)
            Spacer(Modifier.height(10.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "あなたの最初のウィジェット",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
