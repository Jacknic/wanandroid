package com.jacknic.android.wanandroid.ui.page.login

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.twotone.AccountCircle
import androidx.compose.material.icons.twotone.Clear
import androidx.compose.material.icons.twotone.Visibility
import androidx.compose.material.icons.twotone.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.jacknic.android.wanandroid.R
import com.jacknic.android.wanandroid.core.common.loading
import com.jacknic.android.wanandroid.core.common.onError
import com.jacknic.android.wanandroid.core.common.onSuccess
import com.jacknic.android.wanandroid.ui.page.Page
import com.jacknic.android.wanandroid.ui.page.navTop
import com.jacknic.android.wanandroid.ui.page.toMain
import com.jacknic.android.wanandroid.ui.theme.WanandroidTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageLogin(
    nav: NavHostController, vm: LoginViewModel = hiltViewModel<LoginViewModel>()
) {
    val userInfo by vm.userInfo.collectAsStateWithLifecycle()
    val registerResult by vm.registerResult.collectAsStateWithLifecycle()
    var isRegisterMode by rememberSaveable { mutableStateOf(false) }

    // 共享表单状态
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var usernameFocus by remember { mutableStateOf(false) }
    var passwordFocus by remember { mutableStateOf(false) }
    var confirmPasswordFocus by remember { mutableStateOf(false) }
    var visibility by remember { mutableStateOf(false) }
    var confirmVisibility by remember { mutableStateOf(false) }
    var agreedPrivacy by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val skc = LocalSoftwareKeyboardController.current
    val loggingIn = userInfo.loading()
    val registering = registerResult.loading()
    val isLoading = loggingIn || registering
    val privacyNotAgreedMsg = stringResource(R.string.register_error_agree_privacy)

    // 登录结果处理
    userInfo?.onSuccess {
        Toast.makeText(
            context, stringResource(R.string.login_tips_login_success), Toast.LENGTH_SHORT
        ).show()
        nav.toMain()
    }?.onError {
        Toast.makeText(
            context,
            it?.message ?: stringResource(R.string.login_tips_login_failed),
            Toast.LENGTH_SHORT
        ).show()
        vm.resetLoginResult()
    }

    // 注册结果处理
    registerResult?.onSuccess {
        Toast.makeText(
            context, stringResource(R.string.register_tips_success), Toast.LENGTH_SHORT
        ).show()
        nav.toMain()
    }?.onError {
        Toast.makeText(
            context,
            it?.message ?: stringResource(R.string.register_tips_failed),
            Toast.LENGTH_SHORT
        ).show()
        vm.resetRegisterResult()
    }

    // 注册模式下的表单校验
    val usernameError = if (isRegisterMode && (usernameFocus || username.isNotBlank())) {
        LoginViewModel.validateUsername(username)
    } else null

    val passwordError = if (isRegisterMode && (passwordFocus || password.isNotBlank())) {
        LoginViewModel.validatePassword(password)
    } else null

    val confirmPasswordError = if (isRegisterMode && (confirmPasswordFocus || confirmPassword.isNotBlank())) {
        LoginViewModel.validateConfirmPassword(password, confirmPassword)
    } else null

    val passwordStrength = if (isRegisterMode && password.isNotBlank()) {
        LoginViewModel.evaluatePasswordStrength(password)
    } else -1

    Scaffold(
        topBar = {
            TopAppBar(title = { }, actions = {
                TextButton(onClick = {
                    vm.setSkipLogin(true)
                    nav.navTop(Page.Main, Page.Login)
                }) {
                    Text(stringResource(R.string.login_btn_skip_login))
                }
            })
        },
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {

            // 用户名
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { usernameFocus = it.isFocused },
                value = username,
                onValueChange = { username = it },
                label = { Text(stringResource(R.string.login_hit_label_username)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
                ),
                shape = CircleShape,
                enabled = !isLoading,
                leadingIcon = { Icon(Icons.TwoTone.AccountCircle, null) },
                trailingIcon = {
                    if (username.isNotEmpty() && usernameFocus) {
                        IconButton(onClick = { username = "" }) {
                            Icon(imageVector = Icons.Filled.Clear, contentDescription = null)
                        }
                    }
                },
                isError = usernameError != null,
                supportingText = usernameError?.let {
                    { Text(it.message, color = MaterialTheme.colorScheme.error) }
                },
            )

            // 密码
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { passwordFocus = it.isFocused },
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.login_hit_label_password)) },
                singleLine = true,
                visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    imeAction = if (isRegisterMode) ImeAction.Next else ImeAction.Go,
                    keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(
                    onNext = { /* focus moves to confirm password */ },
                    onGo = {
                        if (!loggingIn && username.isNotBlank() && password.isNotBlank()) {
                            vm.login(username, password)
                        }
                        skc?.hide()
                    }
                ),
                shape = CircleShape,
                enabled = !isLoading,
                leadingIcon = {
                    IconButton(onClick = { visibility = !visibility }) {
                        val icon = if (visibility) Icons.TwoTone.Visibility else Icons.TwoTone.VisibilityOff
                        Icon(icon, null)
                    }
                },
                trailingIcon = {
                    if (password.isNotEmpty() && passwordFocus) {
                        IconButton(onClick = { password = "" }) {
                            Icon(imageVector = Icons.TwoTone.Clear, contentDescription = null)
                        }
                    }
                },
                isError = passwordError != null,
                supportingText = passwordError?.let {
                    { Text(it.message, color = MaterialTheme.colorScheme.error) }
                },
            )

            // 注册模式：密码强度 + 确认密码 + 隐私协议
            AnimatedContent(
                targetState = isRegisterMode,
                transitionSpec = {
                    if (targetState) {
                        slideInHorizontally { it } togetherWith slideOutHorizontally { -it }
                    } else {
                        slideInHorizontally { -it } togetherWith slideOutHorizontally { it }
                    }
                },
                label = "register_fields"
            ) { registerMode ->
                if (registerMode) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        // 密码强度指示器
                        if (password.isNotBlank()) {
                            PasswordStrengthIndicator(passwordStrength)
                        }

                        // 确认密码
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { confirmPasswordFocus = it.isFocused },
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text(stringResource(R.string.register_hit_label_confirm_password)) },
                            singleLine = true,
                            visualTransformation = if (confirmVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Go, keyboardType = KeyboardType.Password
                            ),
                            keyboardActions = KeyboardActions(onGo = {
                                skc?.hide()
                            }),
                            shape = CircleShape,
                            enabled = !isLoading,
                            leadingIcon = {
                                IconButton(onClick = { confirmVisibility = !confirmVisibility }) {
                                    val icon = if (confirmVisibility) Icons.TwoTone.Visibility else Icons.TwoTone.VisibilityOff
                                    Icon(icon, null)
                                }
                            },
                            trailingIcon = {
                                if (confirmPassword.isNotEmpty() && confirmPasswordFocus) {
                                    IconButton(onClick = { confirmPassword = "" }) {
                                        Icon(imageVector = Icons.TwoTone.Clear, contentDescription = null)
                                    }
                                }
                            },
                            isError = confirmPasswordError != null,
                            supportingText = confirmPasswordError?.let {
                                { Text(it.message, color = MaterialTheme.colorScheme.error) }
                            },
                        )

                        // 隐私协议
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = agreedPrivacy,
                                onCheckedChange = { agreedPrivacy = it },
                                enabled = !isLoading,
                            )
                            val privacyText = buildAnnotatedString {
                                withStyle(SpanStyle(fontSize = MaterialTheme.typography.bodySmall.fontSize)) {
                                    append(stringResource(R.string.register_agree_privacy))
                                }
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ) {
                                    append(stringResource(R.string.register_privacy_policy))
                                }
                                append("和")
                                withStyle(
                                    SpanStyle(
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                        textDecoration = TextDecoration.Underline
                                    )
                                ) {
                                    append(stringResource(R.string.register_terms_of_service))
                                }
                            }
                            Text(text = privacyText)
                        }
                    }
                } else {
                    Spacer(Modifier.height(0.dp))
                }
            }

            Spacer(Modifier.height(4.dp))

            // 登录/注册按钮
            val loginValid = username.isNotBlank() && password.isNotBlank()
            val registerValid = usernameError == null
                    && passwordError == null
                    && confirmPasswordError == null
                    && agreedPrivacy

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                onClick = {
                    if (isRegisterMode) {
                        if (!agreedPrivacy) {
                            Toast.makeText(context, privacyNotAgreedMsg, Toast.LENGTH_SHORT).show()
                        } else {
                            vm.register(username, password, confirmPassword)
                        }
                    } else {
                        vm.login(username, password)
                    }
                },
                enabled = !isLoading && if (isRegisterMode) registerValid else loginValid,
            ) {
                Text(
                    stringResource(
                        when {
                            isRegisterMode && registering -> R.string.register_btn_register_loading
                            isRegisterMode -> R.string.register_btn_register
                            registering -> R.string.login_btn_login_loading
                            else -> R.string.login_btn_login
                        }
                    )
                )
            }

            // 切换登录/注册模式
            TextButton(onClick = {
                isRegisterMode = !isRegisterMode
                confirmPassword = ""
                agreedPrivacy = false
                confirmPasswordFocus = false
            }) {
                Text(
                    stringResource(
                        if (isRegisterMode) R.string.register_link_to_login
                        else R.string.login_link_to_register
                    )
                )
            }
        }
    }
}

@Composable
private fun PasswordStrengthIndicator(strength: Int) {
    val colorScheme = MaterialTheme.colorScheme
    val (color, label) = when (strength) {
        LoginViewModel.PASSWORD_STRENGTH_WEAK ->
            colorScheme.error to stringResource(R.string.password_strength_weak)
        LoginViewModel.PASSWORD_STRENGTH_MEDIUM ->
            colorScheme.tertiary to stringResource(R.string.password_strength_medium)
        LoginViewModel.PASSWORD_STRENGTH_STRONG ->
            colorScheme.primary to stringResource(R.string.password_strength_strong)
        else -> colorScheme.outline to ""
    }
    val progress = when (strength) {
        LoginViewModel.PASSWORD_STRENGTH_WEAK -> 0.33f
        LoginViewModel.PASSWORD_STRENGTH_MEDIUM -> 0.66f
        LoginViewModel.PASSWORD_STRENGTH_STRONG -> 1f
        else -> 0f
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        LinearProgressIndicator(
            progress = { progress },
            color = color,
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.password_strength_label),
                style = MaterialTheme.typography.labelSmall,
                color = colorScheme.onSurfaceVariant,
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = color,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPageLogin() {
    WanandroidTheme {
        PageLogin(rememberNavController())
    }
}
