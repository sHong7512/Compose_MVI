package com.shong.compose_mvi.presentation.screen.splash

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import com.shong.compose_mvi.presentation.screen.BaseActivity
import com.shong.compose_mvi.presentation.theme.Compose_MVITheme
import com.shong.compose_mvi.presentation.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    private val viewModel: SplashViewModel by viewModels()

    private val multiplePermissionsLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { _ ->
            viewModel.handleEvent(SplashEvent.PermissionResult(getPermissions().isEmpty()))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val state = viewModel.uiState.collectAsState().value
            val event: (SplashEvent) -> Unit = { viewModel.handleEvent(it) }
            LaunchedEffect(Unit) {
                viewModel.effect.collectLatest { effect ->
                    when (effect) {
                        is SplashEffect.StartActivity -> {
                            effect.actBucket.startActivity(this@SplashActivity)
                        }

                        is SplashEffect.ShowToast -> {
                            toast(effect.msg)
                        }
                    }
                }
            }
            Compose_MVITheme {
                SplashScreen(state, event)
            }
        }

        val permissionsToRequest = getPermissions()
        if (permissionsToRequest.isEmpty()) {
            viewModel.handleEvent(SplashEvent.PermissionResult(true))
        } else {
            multiplePermissionsLauncher.launch(permissionsToRequest)
        }
    }


    private fun getPermissions(): Array<String> {
        val permission = mutableListOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // 34
            permission.add(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // 33
            permission.add(Manifest.permission.POST_NOTIFICATIONS)
            permission.add(Manifest.permission.READ_MEDIA_AUDIO)
            permission.add(Manifest.permission.READ_MEDIA_IMAGES)
            permission.add(Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) { // 28
                permission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            permission.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        return permission.filter { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
            .toTypedArray()
    }
}