package com.shong.compose_mvi.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

object DummyData {
    inline fun <reified T> get(context: Context): T {
        val jsonString = try {
            context.assets.open("${T::class.simpleName}.json").bufferedReader()
                .use { it.readText() }
        } catch (e: IOException) {
            Log.e("DummyData_sHong","$e")
            ""
        }

        val gson = Gson()
        val listProductType = object : TypeToken<T>() {}.type
        return gson.fromJson(jsonString, listProductType)
    }
}