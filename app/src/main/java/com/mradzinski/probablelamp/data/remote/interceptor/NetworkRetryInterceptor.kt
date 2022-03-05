package com.mradzinski.probablelamp.data.remote.interceptor

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class NetworkRetryInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val result = runCatching { chain.proceed(request) }
        var response = result.getOrNull()

        if (shouldRetry(response?.code)) {
            var retryCount = 0
            while (shouldRetry(response?.code) && retryCount < 2) {
                retryCount++

                runBlocking { retry(request, chain) }.also { r ->
                    response = r
                }
            }
        }

        return response ?: chain.proceed(request.newBuilder().build())
    }

    private suspend fun retry(
        request: Request,
        chain: Interceptor.Chain
    ): Response? = request
        .newBuilder()
        .build()
        .runCatching {
            delay(2000)
            chain.proceed(this)
        }
        .run { this.getOrNull() }

    private fun shouldRetry(code: Int?): Boolean =
        code == 408 || code == 409 || code == 429 || (code != null && code >= 500) || code == null
}