package com.shong.compose_mvi.presentation.screen.splash

import androidx.lifecycle.viewModelScope
import com.shong.compose_mvi.data.platform.NetworkMonitorService
import com.shong.compose_mvi.domain.usecase.APITestUseCase
import com.shong.compose_mvi.domain.usecase.GetVersionUseCase
import com.shong.compose_mvi.domain.usecase.PostLoginUseCase
import com.shong.compose_mvi.presentation.navigation.ActivityBucket
import com.shong.compose_mvi.presentation.screen.BaseViewModel
import com.shong.compose_mvi.util.logE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// 메인 뷰모델
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getVersionUseCase: GetVersionUseCase,
    private val postLoginUseCase: PostLoginUseCase,
    private val apiTestUseCase: APITestUseCase,
    private val networkMonitor: NetworkMonitorService,
) :
    BaseViewModel<SplashEvent, SplashState, SplashEffect>() {

    override fun createInitialState(): SplashState = SplashState.initial()

    init {
        observeWifiStatus()
    }

    private fun observeWifiStatus() {
        viewModelScope.launch {
            networkMonitor.isWifiConnected.collect { isWifiOn ->
                updateState { it.copy(isWifiAvailable = isWifiOn) }
            }
        }
    }

    override fun handleEvent(event: SplashEvent) {
        when (event) {
            is SplashEvent.PermissionResult -> {
                updateState { it.copy(isPermissionOk = event.isPermissionOk) }
            }

            is SplashEvent.PostLogin -> {
                handlePostLogin(id = event.id, pw = event.pw)
            }

            is SplashEvent.RequestMoveActivity -> {
                setEffect(SplashEffect.StartActivity(actBucket = ActivityBucket.Splash))
            }

            is SplashEvent.RunAPITest -> {
                runAPITest()
            }
        }
        if (event is SplashEvent.RequestMoveActivity) {
            setEffect(SplashEffect.StartActivity(actBucket = ActivityBucket.Splash))
        }
    }

    private fun handlePostLogin(id: String, pw: String) {
        if (id.isEmpty()) {
            setEffect(SplashEffect.ShowToast("아이디를 입력해주세요."))
            return
        }
        if (pw.isEmpty()) {
            setEffect(SplashEffect.ShowToast("비밀번호를 입력해주세요."))
            return
        }

        if (currentState.isRunningLogin) {
            setEffect(SplashEffect.ShowToast("로그인 이미 진행중."))
            return
        }

        viewModelScope.launch {
            updateState { it.copy(isRunningLogin = true) }
            try {
                val loginResult = postLoginUseCase(id, pw)
                loginResult.getOrThrow()
                val versionResult = getVersionUseCase()
                versionResult.getOrThrow()
                setEffect(SplashEffect.StartActivity(actBucket = ActivityBucket.Main))
            } catch (e: Throwable) {
                setEffect(SplashEffect.ShowToast("로그인 실패 [${e.message}]"))
            } finally {
                updateState { it.copy(isRunningLogin = false) }
            }
        }
    }

    fun runAPITest() {
        if (currentState.isRunningAPITest) {
            setEffect(SplashEffect.ShowToast("API 테스트 이미 진행중."))
        }
        viewModelScope.launch {
            try {
                updateState { it.copy(isRunningAPITest = true) }
                apiTestUseCase().getOrThrow()
                setEffect(SplashEffect.ShowToast("API 테스트 성공"))
            } catch (e: Throwable) {
                logE("${e.message}")
                setEffect(SplashEffect.ShowToast("API 테스트 실패 [${e.message}]"))
            } finally {
                updateState { it.copy(isRunningAPITest = false) }
            }
        }
    }

}