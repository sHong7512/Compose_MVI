package com.shong.compose_mvi.util

import android.util.Log
import com.shong.compose_mvi.BuildConfig
import kotlin.jvm.java

private const val TAG_PREFIX = "_sHong"
fun isLogShow(): Boolean = BuildConfig.DEBUG

// --- Verbose ---
fun Any.logV(msg: String) {
    if (isLogShow()) Log.v("${this::class.java.simpleName}${TAG_PREFIX}", msg)
}

fun logV(msg: String) {
    if (isLogShow()) Log.v(TAG_PREFIX, msg)
}

// --- Debug ---
fun Any.logD(msg: String) {
    if (isLogShow()) Log.d("${this::class.java.simpleName}${TAG_PREFIX}", msg)
}

fun logD(msg: String) {
    if (isLogShow()) Log.d(TAG_PREFIX, msg)
}


// --- Information ---
fun Any.logI(msg: String) {
    if (isLogShow()) Log.i("${this::class.java.simpleName}${TAG_PREFIX}", msg)
}

fun logI(msg: String) {
    if (isLogShow()) Log.i(TAG_PREFIX, msg)
}


// --- Warning ---
fun Any.logW(msg: String) {
    if (isLogShow()) Log.w("${this::class.java.simpleName}${TAG_PREFIX}", msg)
}

fun logW(msg: String) {
    if (isLogShow()) Log.w(TAG_PREFIX, msg)
}


// --- Error ---
fun Any.logE(msg: String) {
    if (isLogShow()) Log.e("${this::class.java.simpleName}${TAG_PREFIX}", msg)
}

fun Any.logE(tr: Throwable, msg: String? = null) {
    if (isLogShow()) Log.e(
        "${this::class.java.simpleName}${TAG_PREFIX}",
        msg ?: tr.localizedMessage,
        tr
    )
}

fun logE(msg: String) {
    if (isLogShow()) Log.e(TAG_PREFIX, msg)
}

fun logE(tr: Throwable, msg: String? = null) {
    if (isLogShow()) Log.e(TAG_PREFIX, msg ?: tr.localizedMessage, tr)
}