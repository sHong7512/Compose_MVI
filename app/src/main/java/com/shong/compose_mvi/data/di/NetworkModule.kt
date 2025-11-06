package com.shong.compose_mvi.data.di

import android.util.Log
import com.shong.compose_mvi.BuildConfig
import com.shong.compose_mvi.data.remote.time.TimeAPIInterface
import com.shong.compose_mvi.data.remote.tomo.TomoAPIInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.inject.Named
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor {
            Log.d("RetrofitAPI", it)
        }.apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
        if (BuildConfig.USE_UNSAFE_OKHTTP) {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(c: Array<out X509Certificate>?, a: String?) = Unit
                override fun checkServerTrusted(c: Array<out X509Certificate>?, a: String?) = Unit
                override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
            })
            val sslContext = SSLContext.getInstance("SSL").apply {
                init(null, trustAllCerts, SecureRandom())
            }
            builder
                .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }
        }
        return builder.build()
    }

    @Singleton
    @Provides
    @Named("TomoRetrofit")
    fun provideTomoRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TOMO_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    @Named("TimeRetrofit")
    fun provideTimeRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.TIME_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideTomoAPIInterface(@Named("TomoRetrofit") retrofit: Retrofit): TomoAPIInterface {
        return retrofit.create(TomoAPIInterface::class.java)
    }

    @Singleton
    @Provides
    fun provideTimeAPIInterface(@Named("TimeRetrofit") retrofit: Retrofit): TimeAPIInterface {
        return retrofit.create(TimeAPIInterface::class.java)
    }
}