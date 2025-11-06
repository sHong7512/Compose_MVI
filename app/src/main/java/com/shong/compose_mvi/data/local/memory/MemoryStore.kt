package com.shong.compose_mvi.data.local.memory

import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.Scrap
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.UserInfo
import com.shong.compose_mvi.data.remote.tomo.model.tomonote_info.Version
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

// 메모리에서 갖고 있는 api 데이터 모음
@Singleton
class MemoryStore @Inject constructor() {

    var version: Version? by CachedProperty(onGet = {}, onSet = {})
    var userInfo: UserInfo? by CachedProperty()
    var loginToken: String? by CachedProperty()
    var scrap: Scrap? by CachedProperty()

    fun clear() {
        version = null
        userInfo = null
        loginToken = null
        scrap = null
    }
}

private class CachedProperty<T>(
    private val expirationMillis: Long = 3_600_000L, // 1시간
    private val onGet: ((value: T?) -> Unit)? = null,
    private val onSet: ((value: T?) -> Unit)? = null,
) :
    ReadWriteProperty<Any?, T?> {
    private var value: T? = null
    private var timestamp: Long = -1

    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return if (value != null && (System.currentTimeMillis() - timestamp) < expirationMillis) {
            value
        } else {
            null
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = value
        this.timestamp = if (value != null) System.currentTimeMillis() else -1
    }
}
