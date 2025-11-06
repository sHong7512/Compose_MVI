package com.shong.compose_mvi.data.local.preference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

// 암호화 EncryptedSharedPreference
class EncryptedPref constructor(context: Context) {
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    private val encryptedSharedPrefsFile: String = "Encrypted_Setting"
    private val encryptedPref: SharedPreferences = EncryptedSharedPreferences.create(
        encryptedSharedPrefsFile,
        mainKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun setToken(token: String) {
        with(encryptedPref.edit()) {
            putString("token", token)
            apply()
        }
    }

    fun getToken(): String = encryptedPref.getString("token", "") ?: ""

    fun setUniqueID(uniqueID: String) = encryptedPref.edit().putString("uniqueID", uniqueID).apply()

    fun getUniqueID(): String = encryptedPref.getString("uniqueID", "") ?: ""

}