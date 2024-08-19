package com.uangel.tomonote.data.memory

import com.uangel.tomonote.data.remote.SimpleResponse
import com.uangel.tomonote.data.remote.model.activity.TomonoteActivity
import com.uangel.tomonote.data.remote.model.tomonote.MenuTree
import com.uangel.tomonote.data.remote.model.tomonote_info.Login
import com.uangel.tomonote.data.remote.model.tomonote_info.Version
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

// 메모리에서 갖고 있는 api 데이터 모음
@Singleton
class MemoryStore @Inject constructor() {
    var versionResponse: SimpleResponse<Version>? = null
    var loginResponse: SimpleResponse<Login>? = null
    var menuTreeResponse: SimpleResponse<MenuTree>? = null
    var tomonoteActivityResponse: MutableMap<Int, SimpleResponse<TomonoteActivity>> = mutableMapOf()

    fun clear(){
        versionResponse = null
        loginResponse = null
        menuTreeResponse = null
        tomonoteActivityResponse = mutableMapOf()
    }
}

//@EntryPoint
//@InstallIn(SingletonComponent::class)
//interface MemoryInterface {
//    fun getMemoryStore(): MemoryStore
//}