package com.shong.compose_mvi.data.platform

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkMonitorService @Inject constructor(@ApplicationContext private val context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isWifiConnected: Flow<Boolean> = callbackFlow {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        trySend(capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false)

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            // 네트워크 연결되거나, 연결된 네트워크의 상태가 변경될 때
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities,
            ) {
                trySend(networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            }

            // 네트워크 연결이 끊어졌을 때
            override fun onLost(network: Network) {
                val activeCapabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                trySend(
                    activeCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ?: false
                )
            }
        }
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }.distinctUntilChanged()
}