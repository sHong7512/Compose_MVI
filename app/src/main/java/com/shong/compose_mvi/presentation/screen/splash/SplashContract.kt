package com.shong.compose_mvi.presentation.screen.splash

import com.shong.compose_mvi.presentation.navigation.ActivityBucket
import com.shong.compose_mvi.presentation.screen.UiEffect
import com.shong.compose_mvi.presentation.screen.UiEvent
import com.shong.compose_mvi.presentation.screen.UiState

sealed class SplashEvent : UiEvent {
    data class PermissionResult(val isPermissionOk: Boolean) : SplashEvent()
    data class PostLogin(val id: String, val pw: String) : SplashEvent()
    data class RequestMoveActivity(val actBucket: ActivityBucket) : SplashEvent()
    data object RunAPITest : SplashEvent()
}

data class SplashState(
    val isPermissionOk: Boolean = false,
    val isRunningLogin: Boolean = false,
    val isRunningAPITest: Boolean = false,
    val isWifiAvailable: Boolean = false,
) : UiState {
    companion object {
        fun initial(): SplashState = SplashState()
    }
}

sealed class SplashEffect : UiEffect {
    data class StartActivity(val actBucket: ActivityBucket) : SplashEffect()
    data class ShowToast(val msg: String) : SplashEffect()
}