package com.shong.compose_mvi.presentation.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.shong.compose_mvi.presentation.screen.BaseActivity
import com.shong.compose_mvi.presentation.screen.main.MainActivity
import com.shong.compose_mvi.presentation.screen.splash.SplashActivity
import java.io.Serializable

// 액티비티 정보 담는 클래스. 액티비티 추가시 정의 필수.
sealed class ActivityBucket(
    val activityClass: Class<out BaseActivity>,
) {
    data object Splash : ActivityBucket(activityClass = SplashActivity::class.java)
    data object Main : ActivityBucket(activityClass = MainActivity::class.java)

    fun startActivity(context: Context, extras: List<Pair<String, Any?>> = listOf()) {
        val intent = Intent(context, this.activityClass).apply {
            extras.forEach { (key, value) ->
                when (value) {
                    null -> putExtra(key, null as Serializable?)
                    is Int -> putExtra(key, value)
                    is Long -> putExtra(key, value)
                    is Float -> putExtra(key, value)
                    is Double -> putExtra(key, value)
                    is Boolean -> putExtra(key, value)
                    is String -> putExtra(key, value)
                    is Bundle -> putExtra(key, value)
                    is Parcelable -> putExtra(key, value)
                    is Serializable -> putExtra(key, value)
                    else -> throw IllegalArgumentException("Unsupported type for key: $key. Only primitive types, Parcelable, and Serializable are supported.")
                }
            }

            if (context !is Activity) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        }
        context.startActivity(intent)
    }
}