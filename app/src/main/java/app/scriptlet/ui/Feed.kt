package app.scriptlet.ui

import androidx.compose.foundation.background
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import kotlinx.coroutines.sync.Mutex
import android.content.Context
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeoutOrNull

/**
 * The discovery feed: every bundled preset as a big card with a live
 * preview of the widget it draws — featured first, then the newest
 * additions, then the rest. Browsing is the point — each card actually
 * runs its script so what you see is what the home screen gets.
 * 「入れる」 adds the preset to your scripts and opens it in the editor,
 * where it can be placed on the home screen; a preset whose code is
 * already in your list opens that copy instead.
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
 * Since v0.24.0 the feed is the app's home tab (bottom bar: フィード /
 * マイセット). Cards can be bookmarked with 🔖; bookmarks persist in
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
    onOpenScript: (String) -> Unit,
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
    var bookmarkOnly by remember { mutableStateOf(false) }
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
        topBar = {
            TopAppBar(
                title = { Text("フィード") },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(
                start = Tokens.ScreenPadding,
                end = Tokens.ScreenPadding,
                top = Tokens.ScreenPadding,
                bottom = Tokens.ScreenPadding,
            ),
            verticalArrangement = Arrangement.spacedBy(Tokens.ItemSpacing),
        ) {
            item {
                Column {
                    Text(
                        "気に入ったものを「入れる」と、あなたのスクリプトに加わります。そのままホーム画面にも置けます。🔖 はブックマークです。",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = !bookmarkOnly,
                            onClick = { bookmarkOnly = false },
                            label = { Text("すべて") },
                        )
                        FilterChip(
                            selected = bookmarkOnly,
                            onClick = { bookmarkOnly = true },
                            label = { Text("🔖 ブックマーク (" + bookmarks.size + ")") },
                        )
                    }
                }
            }
            val visible = if (bookmarkOnly) {
                order.filter { SampleScripts.keys[it] in bookmarks }
            } else {
                order
            }
            if (bookmarkOnly && visible.isEmpty()) {
                item {
                    Text(
                        "まだブックマークがありません。カードの「🔖 保存」で集められます。",
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
                FeedCard(
                    name = name,
                    desc = desc,
                    seed = key,
                    result = results[key],
                    added = existing != null,
                    bookmarked = key in bookmarks,
                    onToggleBookmark = { toggleBookmark(key) },
                    loading = autoRun,
                    onAction = {
                        if (existing != null) {
                            onOpenScript(existing.id)
                        } else {
                            val item = store.create(name, code, desc)
                            onOpenScript(item.id)
                        }
                    },
                )
                if (autoRun) {
                    LaunchedEffect(key) {
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
    onToggleBookmark: () -> Unit,
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
            when (result) {
                is RunResult.Success -> WidgetPreview(result.tree)
                is RunResult.Error -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    EmojiTile(name = name, seed = seed, size = 64.dp, emojiSize = 32)
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "プレビューを表示できませんでした",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
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
                }
            }
            Spacer(Modifier.height(Tokens.ItemSpacing))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.weight(1f)) {
                    if (added) {
                        OutlinedButton(onClick = onAction, modifier = Modifier.fillMaxWidth()) {
                            Text("追加済み（開く）", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Button(onClick = onAction, modifier = Modifier.fillMaxWidth()) {
                            Text("入れる", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(Modifier.size(8.dp))
                if (bookmarked) {
                    Button(onClick = onToggleBookmark) {
                        Text("🔖 済み", fontWeight = FontWeight.Bold)
                    }
                } else {
                    OutlinedButton(onClick = onToggleBookmark) {
                        Text("🔖 保存")
                    }
                }
            }
        }
    }
}
