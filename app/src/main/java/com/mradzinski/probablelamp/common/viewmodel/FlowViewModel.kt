package com.mradzinski.probablelamp.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mradzinski.probablelamp.common.AsyncResult
import com.mradzinski.probablelamp.common.Failure
import com.mradzinski.probablelamp.common.Loading
import com.mradzinski.probablelamp.common.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

open class FlowViewModel : ViewModel(), KoinComponent {

    fun <T : Any> Flow<T>.execute(
        subscribeOn: CoroutineDispatcher = Dispatchers.IO,
        subscriber: (AsyncResult<T>) -> Unit
    ) = execute({ it }, subscribeOn, null, subscriber)

    @Suppress("USELESS_CAST")
    fun <T : Any, V> Flow<T>.execute(
        mapper: (T) -> V,
        subscribeOn: CoroutineDispatcher = Dispatchers.IO,
        successMetaData: ((T) -> Any)? = null,
        subscriber: (AsyncResult<V>) -> Unit
    ) {
        viewModelScope.launch {
            subscriber(Loading())
            map { value ->
                val success = Success(mapper(value))
                success.metadata = successMetaData?.invoke(value)
                success as AsyncResult<V>
            }
            .catch { emit(Failure(it)) }
            .flowOn(subscribeOn)
            .collect { asyncData -> subscriber(asyncData) }
        }
    }
}