package com.jacknic.android.wanandroid.ui.page.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
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
import com.jacknic.android.wanandroid.ui.page.toMain
import com.jacknic.android.wanandroid.ui.theme.WanandroidTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageLogin(
    nav: NavHostController, vm: LoginViewModel = hiltViewModel<LoginViewModel>()
) {
    val userInfo by vm.userInfo.collectAsStateWithLifecycle()
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var usernameFocus by remember { mutableStateOf(false) }
    var passwordFocus by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val loggingIn = userInfo.loading()
    val validInput = username.isNotBlank() && password.isNotBlank()
    val skc = LocalSoftwareKeyboardController.current

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
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { }, actions = {
                TextButton(onClick = {
                    nav.toMain()
                }) {
                    Text(stringResource(R.string.login_btn_skip_login))
                }
            })
        },
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                modifier = Modifier.onFocusChanged {
                    usernameFocus = it.isFocused
                },
                value = username,
                onValueChange = { username = it },
                label = { Text(stringResource(R.string.login_hit_label_username)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next, keyboardType = KeyboardType.Text
                ),
                shape = CircleShape,
                enabled = !loggingIn,
                trailingIcon = {
                    if (username.isNotEmpty() && usernameFocus) {
                        IconButton(onClick = { username = "" }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = ""
                            )
                        }
                    }
                },
            )
            OutlinedTextField(
                modifier = Modifier.onFocusChanged {
                    passwordFocus = it.isFocused
                },
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.login_hit_label_password)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Go, keyboardType = KeyboardType.Password
                ),
                keyboardActions = KeyboardActions(onGo = {
                    if (!loggingIn && validInput) {
                        vm.login(username, password)
                    }
                    skc?.hide()
                }),
                shape = CircleShape,
                enabled = !loggingIn,
                trailingIcon = {
                    if (password.isNotEmpty() && passwordFocus) {
                        IconButton(onClick = { password = "" }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = ""
                            )
                        }
                    }
                },
            )
            Button(
                modifier = Modifier
                    .height(50.dp)
                    .width(200.dp),
                onClick = {
                    vm.login(username, password)
                },
                enabled = !loggingIn && validInput,
            ) {
                Text(stringResource(if (loggingIn) R.string.login_btn_login_loading else R.string.login_btn_login))
            }
        }
    }
}

@Preview
@Composable
fun PreviewPageLogin() {
    WanandroidTheme {
        PageLogin(rememberNavController())
    }
}