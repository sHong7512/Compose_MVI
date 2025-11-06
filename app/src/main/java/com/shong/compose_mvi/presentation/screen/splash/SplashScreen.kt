package com.shong.compose_mvi.presentation.screen.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shong.compose_mvi.presentation.component.AutoSizeText
import com.shong.compose_mvi.presentation.component.BasicProgressBar

@Composable
fun SplashScreen(state: SplashState = SplashState.initial(), event: (SplashEvent) -> Unit = {}) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            if (state.isPermissionOk) {
                Column(modifier = Modifier.align(alignment = Alignment.Center)) {
                    var id by remember { mutableStateOf("") }
                    var pw by remember { mutableStateOf("") }

                    val startLogin = {
                        keyboardController?.hide()
                        event.invoke(SplashEvent.PostLogin(id = id, pw = pw))
                    }
                    OutlinedTextField(
                        value = id,
                        onValueChange = { id = it },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ), keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        label = { Text("ID") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = pw,
                        onValueChange = { pw = it },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Send
                        ), keyboardActions = KeyboardActions(
                            onSend = { startLogin.invoke() }
                        ),
                        label = { Text("Password") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    val interactionSource = remember { MutableInteractionSource() }
                    val interactionSource2 = remember { MutableInteractionSource() }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .clip(RoundedCornerShape(10))
                            .background(Color(0x222F3334))
                            .height(40.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = remember {
                                    ripple(bounded = true)
                                },
                                onClick = startLogin
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AutoSizeText(text = "로그인")
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .clip(RoundedCornerShape(10))
                            .background(Color(0x222F3334))
                            .height(40.dp)
                            .clickable(
                                interactionSource = interactionSource2,
                                indication = remember {
                                    ripple(bounded = true)
                                },
                                onClick = {
                                    event.invoke(SplashEvent.RunAPITest)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        AutoSizeText(text = "API 테스트")
                    }
                }
                Text(
                    modifier = Modifier.align(alignment = Alignment.TopStart),
                    text = "와이파이 연결됨 ?? ${state.isWifiAvailable}",
                    style = TextStyle(fontSize = 20.sp)
                )
                if (state.isRunningAPITest || state.isRunningLogin) BasicProgressBar(modifier = Modifier.fillMaxSize())
            } else {
                Text(
                    modifier = Modifier.align(alignment = Alignment.TopCenter),
                    text = "권한 체크 단계",
                    style = TextStyle(fontSize = 40.sp)
                )
                Text(
                    modifier = Modifier.align(alignment = Alignment.Center),
                    text = "권한 허용이 안되어있음!",
                    style = TextStyle(fontSize = 40.sp)
                )
            }
        }
    }
}

@Preview
@Composable
fun SplashPreview() {
    SplashScreen()
}