package com.mradzinski.probablelamp.di

import com.mradzinski.probablelamp.BuildConfig
import com.mradzinski.probablelamp.data.remote.calladapter.FlowCallAdapterFactory
import com.mradzinski.probablelamp.data.remote.ApiClient
import com.mradzinski.probablelamp.data.remote.ApiClientImpl
import com.mradzinski.probablelamp.data.remote.interceptor.NetworkRetryInterceptor
import com.mradzinski.probablelamp.data.remote.ApiService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT_IN_SECONDS = 15L

val networkModule = module(createdAtStart = true) {

    single {
        val builder = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(NetworkRetryInterceptor())

        builder.build()
    }

    factory<Retrofit.Builder> {
        Retrofit.Builder()
            .addCallAdapterFactory(FlowCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(get()))
            .client(get())
    }

    single<ApiService> {
        get<Retrofit.Builder>()
            .baseUrl(BuildConfig.BASE_URL)
            .build()
            .create(ApiService::class.java)
    }

    single<ApiClient> {
        ApiClientImpl(service = get())
    }
}