package app.scriptlet.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.scriptlet.EmojiTile
import app.scriptlet.data.ScriptStore
import app.scriptlet.engine.RunResult
import app.scriptlet.engine.SampleScripts
import app.scriptlet.engine.ScriptEngine
import app.scriptlet.splitLeadingEmoji
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import android.content.Context
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull

/**
 * The discovery feed: every bundled preset as a big card with a live
 * preview of the widget it draws — featured first, then the newest
 * additions, then the rest. Browsing is the point — each card actually
 * runs its script so what you see is what the home screen gets.
 * 「入れる」 adds the preset to your scripts and confirms with a
 * snackbar whose 開く action opens it in the editor (v0.28.0 — it used
 * to jump straight into the editor, which read as "something happened
 * but what?"); the card's button flips to 追加済み（開く）, which opens
 * the copy. A preset whose code is already in your list opens that
 * copy instead.
 *
 * Keeping the feed light with 75 presets: LazyColumn only composes
 * near-visible cards, and preview runs are serialized through [runGate]
 * — one run at a time, and a card that scrolls away while queued is
 * cancelled before it ever runs. Finished results are cached in
 * [results] for the whole visit, so scrolling back never re-runs.
 *
 * Preview runs are keyed "feed-<sample key>" so their Storage (e.g. the
 * weather cache) persists across visits without colliding with the
 * UUID-keyed storage of real scripts.
 *
 * [autoRun] exists for the JVM screenshot tests: they must not start the
 * (native) engine, so their cards stay in the not-run state.
 *
 * Since v0.24.0 the feed is the app's home; since v0.25.0 my set opens
 * from the feed's floating 「📚 マイセット」 button (no bottom tab bar —
 * the nested scaffolds also wasted a band of space at the top). Cards
 * can be bookmarked with 🔖; bookmarks persist in
 * SharedPreferences and the chip at the top filters the feed down to
 * them. The preview cache ([results]) is hoisted to the app level so
 * tab switches never re-run finished previews.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FeedScreen(
    store: ScriptStore,
    engine: ScriptEngine,
    results: androidx.compose.runtime.snapshots.SnapshotStateMap<String, RunResult>,
    runGate: Mutex,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onOpenScript: (String) -> Unit,
    onOpenMySet: () -> Unit = {},
    onHelp: () -> Unit = {},
    autoRun: Boolean = true,
) {
    val scripts by store.scripts.collectAsState()
    val context = LocalContext.current
    val bookmarkPrefs = remember {
        context.getSharedPreferences("feed_bookmarks", Context.MODE_PRIVATE)
    }
    var bookmarks by remember {
        mutableStateOf(bookmarkPrefs.getStringSet("keys", emptySet())?.toSet() ?: emptySet())
    }
    // Preview retry ticks (UX review 2026-11-26): bumping a key's tick
    // re-runs its preview after a failure.
    val retryTicks = remember { androidx.compose.runtime.mutableStateMapOf<String, Int>() }
    // Feed filter: "all", "bm" (bookmarks) or a genre tag (v0.26.0).
    var filter by rememberSaveable { mutableStateOf("all") }
    // Action feedback (v0.28.0): every tap answers. 「入れる」 confirms
    // here instead of silently changing state.
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    fun toggleBookmark(key: String) {
        bookmarks = if (key in bookmarks) bookmarks - key else bookmarks + key
        bookmarkPrefs.edit().putStringSet("keys", bookmarks).apply()
    }
    val order = remember {
        SampleScripts.all.indices.sortedBy { idx ->
            when (SampleScripts.keys[idx]) {
                in SampleScripts.featuredKeys -> 0
                in SampleScripts.freshKeys -> 1
                else -> 2
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("フィード") },
                actions = {
                    androidx.compose.material3.IconButton(onClick = onHelp) {
                        Text(
                            "？",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onOpenMySet,
                icon = { Text("📚") },
                text = { Text("マイセット", fontWeight = FontWeight.Bold) },
            )
        },
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(
                start = Tokens.ScreenPadding,
                end = Tokens.ScreenPadding,
                top = Tokens.ScreenPadding,
                // Room so the last card scrolls clear of the floating
                // my-set button.
                bottom = 96.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing),
        ) {
            item {
                Column {
                    Row(
                        modifier = Modifier.horizontalScroll(androidx.compose.foundation.rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        FilterChip(
                            selected = filter == "all",
                            onClick = { filter = "all" },
                            label = { Text("すべて") },
                        )
                        FilterChip(
                            selected = filter == "bm",
                            onClick = { filter = "bm" },
                            label = { Text("🔖 ブックマーク (" + bookmarks.size + ")") },
                        )
                        for (tag in SampleScripts.tagOrder) {
                            val count = SampleScripts.tags.values.count { tag in it }
                            FilterChip(
                                selected = filter == tag,
                                onClick = { filter = tag },
                                label = { Text(tag + " (" + count + ")") },
                            )
                        }
                    }
                }
            }
            val visible = when (filter) {
                "bm" -> order.filter { SampleScripts.keys[it] in bookmarks }
                "all" -> order
                else -> order.filter { SampleScripts.tags[SampleScripts.keys[it]]?.contains(filter) == true }
            }
            if (visible.isEmpty()) {
                item {
                    Text(
                        if (filter == "bm") {
                            "まだブックマークがありません。カードの「🔖 保存」で集められます。"
                        } else {
                            "このジャンルのものはまだありません。"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            items(visible, key = { SampleScripts.keys[it] }) { index ->
                val key = SampleScripts.keys[index]
                val (name, code) = SampleScripts.all[index]
                val desc = SampleScripts.descs.getOrElse(index) { "" }
                val existing = scripts.firstOrNull { it.code == code }
              Box(
                modifier = Modifier.animateItem(
                    fadeInSpec = null,
                    placementSpec = tween(Motion.MEDIUM2, easing = Motion.Standard),
                    fadeOutSpec = tween(Motion.SHORT4, easing = Motion.StandardAccelerate),
                ),
              ) {
                FeedCard(
                    name = name,
                    desc = desc,
                    seed = key,
                    result = results[key],
                    added = existing != null,
                    bookmarked = key in bookmarks,
                    tags = SampleScripts.tags[key] ?: emptyList(),
                    onToggleBookmark = { toggleBookmark(key) },
                    onRetry = {
                        results.remove(key)
                        retryTicks[key] = (retryTicks[key] ?: 0) + 1
                    },
                    loading = autoRun,
                    onAction = {
                        if (existing != null) {
                            onOpenScript(existing.id)
                        } else {
                            val item = store.create(name, code, desc)
                            scope.launch {
                                val r = snackbarHostState.showSnackbar(
                                    message = "『" + splitLeadingEmoji(name).second + "』をマイセットに追加しました",
                                    actionLabel = "開く",
                                    duration = SnackbarDuration.Short,
                                )
                                if (r == SnackbarResult.ActionPerformed) {
                                    onOpenScript(item.id)
                                }
                            }
                        }
                    },
                )
              }
                if (autoRun) {
                    LaunchedEffect(key, retryTicks[key]) {
                        if (key !in results) {
                            // Serialized: at most one preview run at a time.
                            // withLock is cancellable, so a card that scrolls
                            // away while queued drops out without running.
                            runGate.withLock {
                                if (key !in results) {
                                    val r = withTimeoutOrNull(20_000) {
                                        engine.runDetached("feed-$key", code).await()
                                    } ?: RunResult.Error("プレビューの実行が時間切れになりました", null, emptyList())
                                    results[key] = r
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeedCard(
    name: String,
    desc: String,
    seed: String,
    result: RunResult?,
    added: Boolean,
    bookmarked: Boolean,
    tags: List<String> = emptyList(),
    onToggleBookmark: () -> Unit,
    onRetry: () -> Unit = {},
    loading: Boolean,
    onAction: () -> Unit,
) {
    val (_, restName) = splitLeadingEmoji(name)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Tokens.CardRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        // Preview zone: the widget itself, top-aligned on a soft ground so
        // a tall widget crops at the bottom like an image in a feed.
        Box(
            modifier = Modifier.fillMaxWidth().height(210.dp)
                .clip(RoundedCornerShape(topStart = Tokens.CardRadius, topEnd = Tokens.CardRadius))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(12.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            Crossfade(
                targetState = result,
                animationSpec = tween(Motion.MEDIUM2, easing = Motion.Standard),
                label = "feedPreview",
            ) { current ->
            when (current) {
                is RunResult.Success -> WidgetPreview(current.tree)
                is RunResult.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    EmojiTile(name = name, seed = seed, size = 64.dp, emojiSize = 32)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "プレビューを表示できませんでした",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    androidx.compose.material3.TextButton(onClick = onRetry) {
                        Text("もう一度試す", fontWeight = FontWeight.Bold)
                    }
                }
                else -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    EmojiTile(name = name, seed = seed, size = 64.dp, emojiSize = 32)
                    if (loading) {
                        Spacer(Modifier.height(10.dp))
                        CircularProgressIndicator(modifier = Modifier.size(22.dp), strokeWidth = 2.5.dp)
                    }
                }
            }
            }
        }
        Column(modifier = Modifier.padding(Tokens.CardPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                EmojiTile(name = name, seed = seed, size = 40.dp, emojiSize = 20)
                Spacer(Modifier.size(Tokens.ItemSpacing))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        restName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    if (desc.isNotEmpty()) {
                        Text(
                            desc,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    if (tags.isNotEmpty()) {
                        Text(
                            tags.joinToString(" "),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
            Spacer(Modifier.height(Tokens.ItemSpacing))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    AnimatedContent(
                        targetState = added,
                        transitionSpec = {
                            fadeIn(tween(Motion.MEDIUM1, easing = Motion.StandardDecelerate)) togetherWith
                                fadeOut(tween(Motion.SHORT4, easing = Motion.StandardAccelerate))
                        },
                        label = "addButton",
                    ) { isAdded ->
                        if (isAdded) {
                            OutlinedButton(onClick = onAction, modifier = Modifier.fillMaxWidth()) {
                                Text("追加済み（開く）", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Button(onClick = onAction, modifier = Modifier.fillMaxWidth()) {
                                Text("入れる", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(Modifier.size(8.dp))
                OutlinedButton(onClick = onToggleBookmark) {
                    Text(
                        if (bookmarked) "🔖 済み" else "🔖 保存",
                        fontWeight = if (bookmarked) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
        }
    }
}
