# AGENT.md

## 1. 团队与作者画像 (Developer Profile)

* **经验水平**: 15年 Android 开发与架构经验。
* **设计哲学**: 坚定的 **Clean Architecture** 践行者。追求极致的单向数据流（UDF）、高内聚低耦合、静态代码审查零容忍。
* **Agent 角色定位**: 严谨的资深独立开发者/架构师助理。**绝对禁止**写出临时凑合、未处理异常、或违反 MAD (Modern Android Development) 最佳实践的代码。

---

## 2. 项目概览 (Project Overview)

* **项目名称**: Wanandroid（玩安卓客户端）
* **基础包名**: `com.jacknic.android.wanandroid`
* **API 文档**: 项目根目录 `wanandroid.openapi.json`（OpenAPI 3.0 规范，涵盖所有后端接口定义）
* **仓库地址**: `https://maven.pkg.github.com/Jacknic/wanandroid`
* **CI/CD**: GitHub Actions（`.github/workflows/ci.yml`），自动构建、测试、发布与 Release 产出

---

## 3. 核心技术栈 (Technical Stack)

所有由 Agent 生成或修改的代码，必须严格基于以下技术栈。严禁引入任何过时或非指定依赖：

* **开发语言**: Kotlin (2.2+) - 充分利用 Coroutines、Flow 及严格的空安全。
* **UI 框架**: 100% Jetpack Compose + Material3 - 拒绝任何 XML 布局（包括自定义 View 也必须用 `AbstractComposeView` 桥接）。
* **架构模式**: 现代 MVVM / MVI (基于 Unidirectional Data Flow)。
* **依赖注入**: Google Hilt (基于 Dagger2) - 严格的构造函数注入，KAPT 处理。
* **异步与流**: Kotlin Coroutines + `StateFlow` / `SharedFlow`。
* **网络层**: Retrofit 2 + OkHttp 4 (配置严格的拦截器与超时机制) + Gson 序列化。
* **持久化**: DataStore Preferences (使用 Flow 进行响应式数据监听)。Room Database（预留模块，尚未启用）。
* **图片加载**: Coil 2 (支持 Compose 集成与 SVG)。
* **分页**: Paging 3 (配合 `cachedIn(viewModelScope)` 缓存)。
* **动画**: Lottie Compose (底部导航栏等动画资源)。
* **日志**: Timber + Logger，项目封装为 `TLog` 工具类。
* **工程结构**: 严格的 **Multi-Module（多模块化）** 架构（按 Core 层级拆分）。

---

## 4. 模块架构与依赖规则 (Module Architecture)

### 4.1 模块清单

```
:app                          # 主应用模块（Compose UI、导航、ViewModel）
:apps:legacy                  # 遗留应用壳（ViewBinding，仅占位）

:core:model                   # 纯 Kotlin 数据模型（WanResult, Article, Banner 等）
:core:domain                  # 纯 Kotlin JVM 模块（WanRepository 接口定义）
:core:network                 # 网络层（WanApi Retrofit 接口、OkHttp 配置、Cookie 管理）
:core:data                    # 数据层（Repository 实现、Hilt DataModule 绑定）
:core:datastore               # DataStore 偏好设置（主题、搜索历史、登录状态）
:core:database                # Room 数据库（预留，当前为空壳）
:core:common                  # 公共工具（TLog 日志、StateResult 状态封装、Initializer）
:core:design                  # 设计系统组件（Compose M3 基础组件）
:core:ui                      # 共享 UI 资源（图标、Lottie 动画、字符串）
:core:analytics               # 埋点分析（预留空壳）
:core:notification            # 通知（预留空壳）
:core:testing                 # 测试工具（预留空壳）
:core:data-test               # 测试数据（预留空壳）
:core:datastore-test          # DataStore 测试（预留空壳）

:catalog                      # 版本目录聚合发布模块
```

### 4.2 依赖方向（严格单向，禁止循环）

```
:app → :core:data → :core:domain → :core:model
                    → :core:network → :core:model
                    → :core:datastore → :core:model, :core:common
                    → :core:database
                    → :core:common
:app → :core:design
:app → :core:ui
:app → :core:model
```

**铁律**:
* `:core:domain` 是纯 JVM 模块，**绝对禁止**依赖任何 Android 框架或其他 Core 模块（仅依赖 `:core:model`）。
* `:core:model` 是最底层模块，**绝对禁止**依赖任何其他项目模块。
* `:core:network` 仅依赖 `:core:model`，**禁止**依赖 `:core:domain` 或 `:core:data`。
* 依赖方向: `UI → ViewModel → Repository(interface) → Repository(impl) → Network/DataStore`。

### 4.3 Gradle 约定配置

项目根 `build.gradle.kts` 通过约定方法统一管理所有子模块配置，**禁止**在子模块中重复声明已由约定方法管理的配置：

* `configCommon()` — 所有 Android 模块共享: compileSdk=Baklava(35), minSdk=N(24), Java 8, JVM Toolchain 17, 自动依赖 `:core:common`。
* `configApplication()` — Application 模块扩展: targetSdk=Baklava(35), 签名配置, ProGuard。
* `configLibrary()` — Library 模块扩展: 自动生成 namespace, maven-publish。

**Hilt 自动注入**: 应用了 `com.google.dagger.hilt.android` 插件的模块会自动添加 `hilt-android` + `hilt-android-compiler` 依赖，无需手动声明。

**Feature/App 模块自动依赖**: `parent.name == "feature"` 或 Application 模块自动添加 `:core:model`, `:core:ui`, `:core:design`, `:core:data`, `:core:domain`, `:core:analytics`, `:core:testing` 依赖。

**版本目录**: 所有依赖版本统一在 `gradle/libs.versions.toml` 管理，**禁止**硬编码版本号。通过 `libs.xxx` 访问。

---

## 5. 严苛的架构红线与编码规范 (Architectural Guardrails)

### 5.1 核心红线 (Hard Restrictions)

1. **绝对禁止在 UI 层直接调用 Repository 或进行状态变更**。UI 只能消费 ViewModel 暴露的 UI State，且必须使用 `collectAsStateWithLifecycle()`。
2. **禁止硬编码 String, Color, Dimens**。所有 UI 资源必须走 `Theme.colorScheme`、`stringResource()` 或 `Dimension` 体系。
3. **禁止捕获异常而不处理**。所有的 `catch` 块必须有明确的错误状态上报（`TLog` 记录 + 转化为 UI 层的 `StateResult.Error`）。
4. **禁止混淆 Lifecycle 作用域**。网络请求/数据库操作必须在 `viewModelScope` 或自定义的 `applicationScope` 中启动，严禁在 `MainScope` 或 `GlobalScope` 中盲目启动。
5. **禁止在 `:core:domain` 中引入 Android 依赖**。Domain 层必须是纯 Kotlin JVM 模块。
6. **禁止跨模块直接引用实现类**。必须面向接口编程（如 `WanRepository` 接口在 domain，实现在 data）。

### 5.2 响应式架构与状态管理 (UDF / MVI)

每个屏幕（Screen）必须由且仅由一个主 `UiState`（`StateFlow`）驱动。

* **状态不透明性**: 所有的 `MutableStateFlow` 必须保持为 ViewModel 私有（`_xxx`），对外仅暴露只读的 `StateFlow`。
* **副作用处理**: 一次性事件（如弹窗、页面跳转）必须通过 `Channel` 或 `SharedFlow` 封装为 `UiEffect` / `UiEvent` 发送。
* **加载状态封装**: 使用项目统一的 `StateResult<T>` 密封接口（`Loading` / `Success` / `Error`），配合 `toStateResult()` 扩展函数将 `Result<T>` 转换。

```kotlin
// 正确示例：ViewModel 状态暴露标准
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repo: WanRepository
) : ViewModel() {
    private val log = TLog.create("HomeViewModel", BuildConfig.DEBUG)

    private val _bannerList = MutableStateFlow<StateResult<List<Banner>>>(StateResult.Loading)
    val bannerList = _bannerList.asStateFlow()

    private fun getBannerList() {
        viewModelScope.launch {
            _bannerList.emit(repo.getHomeBannerList().toStateResult())
        }
    }
}
```

### 5.3 数据层规范 (Data Layer)

* **Repository 接口**: 定义在 `:core:domain`，所有方法返回 `Result<T>`。
* **Repository 实现**: 定义在 `:core:data`，标记为 `internal` + `@Singleton`，通过 Hilt `@Binds` 绑定。
* **网络请求封装**: 使用 `runResult {}` 扩展函数统一将 `WanResult<T>` 转换为 `Result<T>`，自动处理 HTTP 异常和业务错误。

```kotlin
// 正确示例：Repository 实现标准
@Singleton
internal class DefaultWanRepository @Inject constructor(private val api: WanApi) : WanRepository {
    override suspend fun getHomeBannerList() = runResult(api::getBannerList)
}
```

* **Hilt Module 绑定**:

```kotlin
// 正确示例：DataModule
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindWanRepository(impl: DefaultWanRepository): WanRepository
}
```

* **DataStore 操作**: 通过 `UserPreferencesDataSource` 封装，返回 `Flow<T>` 供 Repository 消费，**禁止**在 ViewModel 中直接访问 DataStore。

### 5.4 网络层规范 (Network Layer)

* **API 接口定义**: 在 `WanApi` 接口中使用 Retrofit 注解，返回 `WanResult<T>`。
* **Cookie 管理**: 使用 `AndroidCookieJar` 自动管理登录态 Cookie。
* **SSL 信任**: 使用 `AppTrustManager` 处理证书信任。
* **自定义注解**: 使用 `@PageNotice` 等项目自定义注解标记分页接口。
* **Base URL**: 由 `NetworkModule` 统一提供，禁止硬编码。

### 5.5 导航规范 (Navigation)

* **路由常量**: 统一定义在 `Page` 对象中，禁止硬编码路由字符串。
* **导航扩展**: 使用 `navTop()`、`toMain()`、`openBrowser()` 等扩展函数进行页面跳转。

```kotlin
// 正确示例：路由定义与导航
object Page {
    const val Main = "PageMain"
    const val Splash = "PageSplash"
    const val Search = "PageSearch"
    const val Browser = "PageBrowser"
    const val Setting = "PageSetting"
    const val Login = "PageLogin"
}

// 导航跳转
val nav = LocalNavCtrl.current
nav.navigate(Page.Search)
nav.toMain()  // 清空栈跳转首页
nav.openBrowser(url)  // 打开浏览器
```

### 5.6 主题与样式规范 (Theme & Styling)

* **主题入口**: 使用 `WanandroidTheme {}` 包裹根 Composable。
* **颜色获取**: 通过 `MaterialTheme.colorScheme` 获取，禁止硬编码颜色值。
* **主题模式**: `ThemeMode` 枚举（SYSTEM / LIGHT / DARK），通过 `useThemeMode()` 切换。
* **颜色方案**: `ThemeColorScheme` 枚举（DEFAULT / GREEN / PURPLE / ORANGE / RED / CUSTOM），通过 `useThemeColorScheme()` 切换。
* **动态颜色**: Android 12+ 支持 Material You 动态取色，通过 `useDynamicThemeColor()` 开关。
* **自定义颜色**: `CustomColorData` 自动从主色派生 secondary / tertiary。

### 5.7 日志规范 (Logging)

所有日志必须通过项目封装的 `TLog` 工具类输出，**严禁**直接使用 `android.util.Log`、`println` 或裸 `Timber` 调用：

```kotlin
// 正确示例
private val log = TLog.create("HomeViewModel", BuildConfig.DEBUG)
log.tag().d("getArticleListFlow: 创建分页流 cid=$cid")
log.tag().e("请求失败: ${e.message}")

// 错误示例 ❌
Log.d("TAG", "message")
Timber.d("message")
println("message")
```

* `TLog.create(tag, isLoggable)` — `isLoggable=true` 时忽略全局日志等级过滤，适用于调试期间。
* Release 构建自动提升日志等级为 `INFO`，Debug 构建为 `VERBOSE`。

### 5.8 分页规范 (Paging)

* 使用 Paging 3 + `Flow<PagingData<T>>` 进行分页数据管理。
* 分页流必须在 ViewModel 中通过 `.cachedIn(viewModelScope)` 缓存，避免配置变更导致重新加载。
* 使用项目封装的 `PagingListDataSource.pager {}` 创建分页 Pager。

```kotlin
// 正确示例：分页流创建
fun getArticleListFlow(cid: Int): Flow<PagingData<Article>> {
    return pagingFlows.getOrPut(cid) {
        PagingListDataSource.pager(
            loadAction = { page, pageSize ->
                repo.getHomeArticleList(page, pageSize, null)
            }
        ).flow.cachedIn(viewModelScope)
    }
}
```

### 5.9 单元测试要求 (Testing Standard)

* 所有新增的 `ViewModel` 和 `Repository` 必须同步生成对应的单元测试（Unit Test）。
* **Mock 框架**: 统一使用 `MockK`，禁止使用 `Mockito`。
* **异步测试**: 使用 `kotlinx-coroutines-test` 中的 `StandardTestDispatcher`，并使用 `cashapp/turbine` 来测试 Flow 的发射序列。
* **测试模块**: 使用 `:core:testing` 模块提供共享测试基础设施。

---

## 6. 命名约定 (Naming Conventions)

| 类别 | 约定 | 示例 |
|------|------|------|
| ViewModel | `XxxViewModel` | `HomeViewModel`, `LoginViewModel` |
| UI State | `XxxUiState` | `DetailUiState` |
| UI Effect | `XxxUiEffect` / `XxxUiEvent` | `LoginUiEffect` |
| 页面 Composable | `PageXxx` | `PageHome`, `PageLogin` |
| 组件 Composable | 功能描述命名 | `ArticleListItem`, `HomeBanner` |
| 路由常量 | `Page.Xxx` | `Page.Main`, `Page.Search` |
| Repository 接口 | `XxxRepository` | `WanRepository` |
| Repository 实现 | `DefaultXxxRepository` (internal) | `DefaultWanRepository` |
| Hilt Module | `XxxModule` | `DataModule`, `NetworkModule` |
| StateFlow 私有 | `_xxx` 前缀 | `_bannerList` |
| StateFlow 公开 | 无下划线 | `bannerList` |
| 数据模型 | 业务语义命名 | `Article`, `Banner`, `WanResult` |
| 网络响应包装 | `WanResult<T>` | `WanResult<Paging<Article>>` |
| 日志标签 | 类名 | `TLog.create("HomeViewModel")` |
| 资源文件 | snake_case | `ic_launcher`, `bg_splash` |

---

## 6.1 国际化规范 (Internationalization Standards)

所有用户可见的文本**必须**通过 `strings.xml` 资源管理，严禁硬编码中文文本。

### 6.1.1 字符串资源命名规范

采用 `模块_描述` 的下划线命名法：

| 类别 | 前缀 | 示例 |
|------|------|------|
| 通用操作 | `action_` | `action_confirm`, `action_cancel` |
| 页面标题 | `page_` | `page_collection`, `page_setting` |
| 功能模块 | `模块名_` | `collection_`, `todo_`, `setting_` |
| 错误提示 | `error_` | `error_network`, `error_unknown` |
| 成功提示 | `success_` | `success_collect`, `success_delete` |
| 状态文本 | `状态_` | `loading`, `no_more_data` |

### 6.1.2 代码开发规范

**禁止硬编码文本**：
```kotlin
// ❌ 错误 - 硬编码
Text("确定要删除吗？")
Button(onClick = { }) { Text("删除") }

// ✅ 正确 - 使用资源引用
Text(stringResource(R.string.collection_delete_message))
Button(onClick = { }) { Text(stringResource(R.string.action_delete)) }
```

**必需的导入**：
```kotlin
import androidx.compose.ui.res.stringResource
```

**带参数的字符串**：
```xml
<!-- strings.xml -->
<string name="collection_selected_count">已选 %1$d 项</string>
```
```kotlin
// 使用
Text(stringResource(R.string.collection_selected_count, selectedCount))
```

### 6.1.3 开发流程

1. **在 `core/ui/src/main/res/values/strings.xml` 中定义字符串资源**
2. **在代码中通过 `stringResource(R.string.xxx)` 引用**
3. **如果使用参数，确保 XML 中的占位符类型正确**（`%1$d` = 整数，`%1$s` = 字符串）

### 6.1.4 代码审查检查点

- [ ] 所有用户可见文本是否使用 `stringResource` 引用
- [ ] 是否导入了 `androidx.compose.ui.res.stringResource`
- [ ] 字符串资源命名是否符合规范
- [ ] 是否存在硬编码的中文字符串

### 6.1.5 参考文档

详细规范请参考：`docs/internationalization_guide.md`

---

## 7. 高频构建与自动化命令 (Engineering Commands)

Agent 在辅助进行代码审查、重构或本地验证时，应熟知并能准确建议/执行以下 Gradle 命令（在项目根目录下通过 `./gradlew` 执行）：

### 7.1 代码质量与格式化

* **运行静态代码检查**: `./gradlew detekt` (严格执行团队自定义的 detekt 规则)
* **代码格式化检查**: `./gradlew spotlessCheck`
* **自动应用代码格式化**: `./gradlew spotlessApply` (在提交代码或让 Agent 生成大批量代码后必跑)

### 7.2 编译与构建

* **清理并编译 Debug 包**: `./gradlew clean assembleDebug`
* **编译全模块 Release 混淆包**: `./gradlew assembleRelease --no-build-cache`
* **查看依赖冲突/依赖树**: `./gradlew :app:dependencies --configuration implementation`

### 7.3 自动化测试

* **运行全项目单元测试**: `./gradlew testDebugUnitTest`
* **运行指定模块的测试**: `./gradlew :core:data:testDebugUnitTest`
* **生成本地测试覆盖率报告**: `./gradlew koverHtmlReport`

### 7.4 发布

* **发布到 GitHub Packages**: `./gradlew publish` (需配置 `GITHUB_ACTOR` 和 `GITHUB_TOKEN` 环境变量)
* **版本号**: 基于 Git Tag 自动生成，无 Tag 时使用 `0.0.1-SNAPSHOT`

---

## 8. CI/CD 流水线 (CI/CD Pipeline)

GitHub Actions 工作流（`.github/workflows/ci.yml`）在以下时机触发：

* **Push / PR 到 main 分支**: 自动构建 + 运行单元测试 + 发布 Maven 产物
* **Git Tag 推送**: 构建 Release APK 并创建 GitHub Release（附带 APK 产物）

**Agent 注意事项**:
* 修改代码后应确保 `./gradlew assembleDebug` 可通过，避免破坏 CI。
* 新增依赖需确认在 `libs.versions.toml` 中注册，否则 CI 可能因版本解析失败而中断。

---

## 9. 交互准则 (Agent Interaction Protocol)

1. **代码精简优先**: 生成代码时，无需解释基础的 Kotlin/Compose 语法。请直接给出符合上述严苛架构的、生产环境可用的完整代码或精准 Diff。
2. **主动重构提示**: 如果在修改代码时发现旧代码违反了本 `AGENT.md` 的红线（例如在 Compose 中发现了老旧的 `LiveData` 或硬编码颜色），请在修改当前任务的同时，主动给出重构建议。
3. **日志规范**: 生成的任何调试日志，必须使用项目封装的 `TLog.create(TAG, isLoggable).tag().d(message)`，严禁直接使用原生的 `Log.d`、`Timber.d` 或 `println`。
4. **API 参照**: 新增或修改网络接口时，必须参照 `wanandroid.openapi.json` 中的接口定义，确保路径、参数、响应类型与规范一致。
5. **模块归属判断**: 新增代码时，先判断应归属哪个模块。ViewModel/页面 → `:app`；数据模型 → `:core:model`；Repository 接口 → `:core:domain`；网络接口 → `:core:network`；Repository 实现 → `:core:data`；偏好设置 → `:core:datastore`；通用 UI 组件 → `:core:design`。
6. **构建验证**: 生成大批量代码后，建议执行 `./gradlew spotlessApply` 格式化，并确认 `./gradlew assembleDebug` 可通过。
