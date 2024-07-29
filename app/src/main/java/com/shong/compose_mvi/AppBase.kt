package com.shong.compose_mvi

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp

/**
 * @author SoonHong Kwon
 * Compose + MVI pattern
 * kotlin 2.0
 * DI => hilt
 */

fun isLogShow(): Boolean = BuildConfig.DEBUG

fun Any.logD(msg: String) { if (isLogShow()) Log.d("${this::class.java.simpleName}_sHong", msg) }
fun Any.logW(msg: String) { if (isLogShow()) Log.w("${this::class.java.simpleName}_sHong", msg) }
fun Any.logE(msg: String) { if (isLogShow()) Log.e("${this::class.java.simpleName}_sHong", msg) }
fun logD(tag: String, msg: String) { if (isLogShow()) Log.d("${tag}_sHong", msg) }
fun logW(tag: String, msg: String) { if (isLogShow()) Log.w("${tag}_sHong", msg) }
fun logE(tag: String, msg: String) { if (isLogShow()) Log.e("${tag}_sHong", msg) }

private var toast: Toast? = null
fun Context.toast(message: String) {
    toast?.cancel()
    toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast?.show()
}

@HiltAndroidApp
class AppBase : Application()