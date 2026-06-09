# 国际化规范指南

## 1. 概述

本文档定义了 Wanandroid 项目的国际化（i18n）标准规范，确保所有用户可见的文本都通过资源文件管理，支持多语言切换。

## 2. 资源文件结构

### 2.1 资源文件位置
- **主资源文件**: `core/ui/src/main/res/values/strings.xml`
- **语言特定资源**: `core/ui/src/main/res/values-xx/strings.xml` (如 `values-zh-rCN`, `values-en`)

### 2.2 命名规范

#### 字符串资源命名规则
采用 `模块_描述` 的下划线命名法，分为以下类别：

| 类别 | 前缀 | 示例 |
|------|------|------|
| 通用操作 | `action_` | `action_confirm`, `action_cancel` |
| 页面标题 | `page_` | `page_collection`, `page_setting` |
| 功能模块 | `模块名_` | `collection_`, `todo_`, `setting_` |
| 错误提示 | `error_` | `error_network`, `error_unknown` |
| 成功提示 | `success_` | `success_collect`, `success_delete` |
| 状态文本 | `状态_` | `loading`, `no_more_data` |

#### 命名示例
```xml
<!-- 通用操作 -->
<string name="action_confirm">确定</string>
<string name="action_cancel">取消</string>

<!-- 收藏模块 -->
<string name="collection_title">我的收藏</string>
<string name="collection_empty">暂无收藏</string>

<!-- 错误提示 -->
<string name="error_network">网络错误，请检查网络连接</string>
```

## 3. 代码开发规范

### 3.1 禁止硬编码文本

❌ **错误示例** - 硬编码中文文本：
```kotlin
Text("确定要删除吗？")
Button(onClick = { }) {
    Text("删除")
}
```

✅ **正确示例** - 使用资源引用：
```kotlin
Text(stringResource(R.string.collection_delete_message))
Button(onClick = { }) {
    Text(stringResource(R.string.action_delete))
}
```

### 3.2 必需的导入

使用 `stringResource` 前必须导入：
```kotlin
import androidx.compose.ui.res.stringResource
```

### 3.3 带参数的字符串

#### XML 定义
```xml
<!-- %1$d = 整数, %1$s = 字符串, %1$f = 浮点数 -->
<string name="collection_selected_count">已选 %1$d 项</string>
<string name="setting_current_version">当前版本: v%1$s</string>
```

#### 代码中使用
```kotlin
// 单个参数
Text(stringResource(R.string.collection_selected_count, selectedCount))

// 多个参数
Text(stringResource(R.string.collection_batch_delete_partial, successCount, failCount))
```

### 3.4 格式化复杂文本

对于需要拼接的动态文本，优先使用资源参数：
```kotlin
// ❌ 避免
Text("已删除 ${result.successCount} 篇收藏")

// ✅ 推荐
Text(stringResource(R.string.collection_batch_delete_success, result.successCount))
```

## 4. 开发流程

### 4.1 新增文本的步骤

1. **在 `strings.xml` 中定义字符串资源**
   ```xml
   <string name="module_action">按钮文本</string>
   ```

2. **在代码中引用**
   ```kotlin
   Text(stringResource(R.string.module_action))
   ```

3. **如果使用参数，确保格式匹配**
   ```xml
   <string name="message_with_param">你好，%1$s</string>
   ```

### 4.2 代码审查检查点

- [ ] 所有用户可见文本是否使用 `stringResource` 引用
- [ ] 是否导入了 `androidx.compose.ui.res.stringResource`
- [ ] 字符串资源命名是否符合规范
- [ ] 带参数的字符串是否正确使用格式化占位符
- [ ] 是否存在硬编码的中文字符串

## 5. 常见错误与修复

### 5.1 忘记导入 stringResource
**错误**: `Unresolved reference: stringResource`

**修复**: 添加导入
```kotlin
import androidx.compose.ui.res.stringResource
```

### 5.2 参数类型不匹配
**错误**: `Wrong argument type`

**修复**: 检查 XML 中的占位符类型
- `%1$d` = Int
- `%1$s` = String
- `%1$f` = Float

### 5.3 硬编码文本检测
使用以下命令搜索硬编码文本：
```powershell
# 搜索 Kotlin 文件中的中文字符串
Select-String -Path "**/*.kt" -Pattern '"[\u4e00-\u9fa5]+"' -Encoding UTF8
```

## 6. 多语言支持

### 6.1 添加新语言

1. 创建对应的资源目录，如 `values-en` 用于英语
2. 复制 `strings.xml` 并翻译成对应语言
3. 测试语言切换功能

### 6.2 语言切换实现

项目使用 `AppCompatDelegate` 实现动态语言切换：
```kotlin
// 设置语言
val locales = LocaleListCompat.forLanguageTags("zh")
AppCompatDelegate.setApplicationLocales(locales)

// 获取当前语言
val currentLocales = AppCompatDelegate.getApplicationLocales()
```

## 7. 工具支持

### 7.1 Android Studio
- 使用 `Extract string resource` 快速提取硬编码文本
- 右键点击硬编码文本 → `Refactor` → `Extract string resource`

### 7.2 自动化检查
建议添加 lint 检查规则，禁止硬编码文本：
```gradle
// build.gradle.kts
lintOptions {
    enable 'HardcodedText'
}
```

## 8. 示例参考

### 8.1 完整示例 - 对话框

```kotlin
@Composable
fun ConfirmDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.action_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
    )
}
```

### 8.2 使用资源

```kotlin
// 调用处
ConfirmDialog(
    title = stringResource(R.string.collection_delete_title),
    message = stringResource(R.string.collection_delete_message),
    onConfirm = { /* handle confirm */ },
    onDismiss = { /* handle dismiss */ },
)
```

## 9. 更新日志

| 日期 | 版本 | 更新内容 |
|------|------|----------|
| 2026-06-09 | 1.0 | 初始版本，定义国际化规范 |
| 2026-06-09 | 1.1 | 完善 PageCollection、PageTodo、PageSetting 的国际化 |
