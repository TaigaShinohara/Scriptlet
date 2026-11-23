# UI検証（実機なし検証の標準）— Scriptlet

このVMにはエミュレータが無いため、交付前に画面をJVM上で撮影・照合する。
対象テスト: `app/src/test/java/app/scriptlet/ScreenshotTest.kt`（Paparazzi 1.3.5）

カバー画面（標準の最低ライン）:
1. 起動直後の画面 — スクリプト一覧（初回シード＝テンプレ2本）
2. オンボーディング入口 — 1ページ目（フック）
3. その版で作り替えた画面 — 編集画面（天気テンプレ読み込み状態）

ゴールデン: `app/src/test/snapshots/images/*.png`（record で生成、verify で照合）

## 実行方法（このVM専用の事情つき）

Gradle の `testDebugUnitTest` はこのVMでは動かない。テストワーカーが
ビルドプロセスとループバック通信する経路をVM側が遮断するため、ワーカーが
テスト発見前に死ぬ（SuiteTestClassProcessor の NPE が兆候）。
他プロジェクトと同じく **JUnitCore 直接実行** が正本:

```sh
.local/run-screenshot-tests.sh record   # ゴールデン再生成
.local/run-screenshot-tests.sh verify   # 照合
```

前提: アプリとテストのコンパイル＋ASM変換まで終わっていること。

```sh
./build.sh assembleDebug compileDebugUnitTestKotlin \
           transformDebugClassesWithAsm transformDebugUnitTestClassesWithAsm
```

## クラスパスの注意（.local/test-cp.txt）

ランナーは `.local/test-cp.txt` のクラスパスを使う。中身は Gradle ワーカーの
引数ファイル（`~/workspace/tools/gradle-home/.tmp/gradle-worker-classpath*` の
うち scriptlet を含む最新もの）の2行目が正本。ただし2点だけ差し替える:

1. **android.jar**: Paparazzi 1.3.5 同梱の layoutlib は API 34 世代で、
   compileSdk 36 の android.jar にある `Build$VERSION_CODES_FULL` を
   処理できず `Renderer.configureBuildProperties` が落ちる。
   → SDK の **android-35** スタブ jar に差し替える。
2. **アプリ本体 classes.jar**: transforms キャッシュ側は古いことがある。
   → `app/build/intermediates/runtime_app_classes_jar/debug/bundleDebugClassesToRuntimeJar/classes.jar`
     （最新の assembleDebug で再生成されたもの）に差し替える。

依存を変えたら、テストを1回 Gradle で走らせて（ワーカーは死ぬが引数ファイルは
残る）新しい引数ファイルから取り直す。

## テスト側の環境対策（ScreenshotTest 内）

- layoutlib の `_Original_Build.FINGERPRINT` 等が null で、Compose の
  PrefetchScheduler 静的初期化が NPE になる → `@BeforeClass` で
  Unsafe 経由で無害な文字列を埋める。
- Paparazzi 環境には AppWidgetManager が無い（一覧画面が null で落ちる）
  → 本体側 `ListScreen` に null ガードを入れてある（実機でも無害）。

## 交付前チェック（今後の版でも毎回）

1. 上記手順で verify を実行し、全件合格を確認する。
2. ゴールデン3枚を目視する（真っ白・崩壊・見切れが無いこと）。
3. 画面を作り替えた版では、その画面のゴールデンを record し直して差分を確認する。
