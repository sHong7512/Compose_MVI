package com.shong.compose_mvi.data.preference

import android.content.Context
import android.content.SharedPreferences

class SettingPref constructor(context: Context) {
    private val pref: SharedPreferences =
        context.getSharedPreferences("Basic_Setting", Context.MODE_PRIVATE)

    fun setUniqueID(uniqueID: String) = pref.edit().putString("uniqueID", uniqueID).apply()

    fun getUniqueID(): String = pref.getString("uniqueID", "") ?: ""
}