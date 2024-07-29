package com.shong.compose_mvi.data.remote.retrofitClient

import com.shong.compose_mvi.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class TimeRetrofitApi constructor(private val httpLoggingInterceptor: HttpLoggingInterceptor) {
    private val baseUrl: String = BuildConfig.BASE_URL
    private var ignoreBrowserCookie = true

    companion object {
        // 안전하지 않은 연결 허용
        fun getUnsafeOkHttpClient(interceptors: List<Interceptor>): OkHttpClient.Builder {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?,
                ) = Unit

                override fun checkServerTrusted(
                    chain: Array<out X509Certificate>?,
                    authType: String?,
                ) = Unit

                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })

            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            val sslSocketFactory = sslContext.socketFactory

            val builder = OkHttpClient.Builder()
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier { hostname, session -> true }
            for (itc in interceptors) {
                builder.addInterceptor(itc)
            }

            return builder
        }
    }

    fun createClient(): TimeInterface {
        val okHttpClient: OkHttpClient =
            if (ignoreBrowserCookie) getUnsafeOkHttpClient(listOf(httpLoggingInterceptor)).build()
            else OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(TimeInterface::class.java)
    }
}