package com.mradzinski.probablelamp.data.remote.calladapter

import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.coroutines.resumeWithException

class FlowCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Flow::class.java != getRawType(returnType)) {
            return null
        }

        if (returnType !is ParameterizedType) {
            error("Flow return type must be parameterized as Flow<Foo>")
        }

        val responseType = getParameterUpperBound(0, returnType)

        return BodyCallAdapter<Any>(responseType)
    }
}

private class BodyCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<T>> {

    override fun responseType(): Type = responseType

    @OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.InternalCoroutinesApi::class)
    override fun adapt(call: Call<T>): Flow<T> = flow {
        emit(suspendCancellableCoroutine { continuation ->
            call.registerCallback(continuation) { response ->
                continuation.resumeWith(kotlin.runCatching {
                    if (response.isSuccessful) {
                        response.body() ?: throw NullPointerException("Response body is null: $response")
                    } else {
                        throw HttpException(response)
                    }
                })
            }

            call.registerOnCancellation(continuation)
        })
    }
}

private fun Call<*>.registerOnCancellation(continuation: CancellableContinuation<*>) {
    continuation.invokeOnCancellation {
        try {
            cancel()
        } catch (e: Exception) {
            // Ignore cancel exception
        }
    }
}

private fun <T> Call<T>.registerCallback(continuation: CancellableContinuation<*>, success: (response: Response<T>) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) = success(response)
        override fun onFailure(call: Call<T>, t: Throwable) = continuation.resumeWithException(t)
    })
}