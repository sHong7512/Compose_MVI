package com.shong.compose_mvi

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp

/**
 * @author SoonHong Kwon
 * Compose + MVI pattern with kotlin 2.0
 * DI - hilt 적용
 * reference :: https://proandroiddev.com/mvi-architecture-with-kotlin-flows-and-channels-d36820b2028d
 */

fun Any.logD(msg: String) = Log.d("${this::class.java.simpleName}_sHong", msg)
fun Any.logW(msg: String) = Log.w("${this::class.java.simpleName}_sHong", msg)
fun Any.logE(msg: String) = Log.e("${this::class.java.simpleName}_sHong", msg)

private var toast: Toast? = null
fun Context.toast(message: String) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast?.show()
}

@HiltAndroidApp
class AppBase : Application()