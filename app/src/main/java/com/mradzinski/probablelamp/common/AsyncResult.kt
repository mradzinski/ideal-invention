package com.mradzinski.probablelamp.common

import java.io.Serializable

/**
 * Result of an Async operation such as a network request, I/O operation, etc.
 *
 * Known subclasses are: [Success], [Failure], [Loading], and [Uninitialized]
 *
 * Example:
 * ```
 * // myOperation returns a subclass of AsyncResult (Success, Fail, etc).
 * val result: AsyncResult<String> = myOperation()
 *
 * when(result) {
 *     is Uninitialized -> Unit
 *     is Loading       -> Unit
 *     is Success       -> Unit
 *     is Fail          -> Unit
 * }
 *
 * // Or when Uninitialized and Loading lead to the same behavior:
 *
 * when(result) {
 *     is Loading -> Unit
 *     is Success    -> Unit
 *     is Fail       -> Unit
 * }
 * ```
 */
sealed class AsyncResult<out T>(val complete: Boolean, val shouldLoad: Boolean, private val value: T?): Serializable {

    /**
     * Returns the Success value or null.
     *
     * Success always have a value. Loading and Fail can also return a value which is useful for
     * pagination or progressive data loading.
     *
     * Can be invoked as an operator like: `yourProp()`
     */
    open operator fun invoke(): T? = value

    companion object {
        /**
         * Helper to set metadata on a Success instance.
         * @see Success.metadata
         */
        fun <T> Success<*>.setMetadata(metadata: T) {
            this.metadata = metadata
        }

        /**
         * Helper to get metadata on a Success instance.
         * @see Success.metadata
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> Success<*>.getMetadata(): T? = this.metadata as T?
    }
}

/**
 * [AsyncResult] Loading state.
 */
@Suppress("CanBeParameter")
class Loading<out T>(private val value: T? = null) : AsyncResult<T>(complete = false, shouldLoad = false, value = value) {
    override fun equals(other: Any?) = other is Loading<*>

    override fun hashCode() = "Loading".hashCode()
}

/**
 * [AsyncResult] Success state. You can retrieve the value wrapped by this [Success] instance by doing:
 *
 * ```
 * val result = Success<String>()
 * val value = result() // which is the short for result.invoke()
 * ```
 *
 * or by doing:
 *
 * ```
 * val result = Success<String>()
 * val value = result.getWrappedValue()
 * ```
 */
data class Success<out T>(private val value: T) : AsyncResult<T>(complete = true, shouldLoad = false, value = value) {

    override operator fun invoke(): T = value

    /**
     * Get the value of type [T] wrapped by this instance of [Success]
     */
    fun getWrappedValue(): T = value

    /**
     * Optional information about the value.
     * This is intended to support tooling (eg logging).
     * It allows data about the original Observable to be kept and accessed later. For example,
     * you could map a network request to just the data you need in the value, but your base layers could
     * keep metadata about the request, like timing, for logging.
     *
     * @see AsyncResult.setMetadata
     * @see AsyncResult.getMetadata
     */
    var metadata: Any? = null
}

/**
 * [AsyncResult] Fail state. You can retrieve the error by doing:
 * ```
 * val result = Fail<String>()
 * val error = result.error
 * ```
 */
data class Failure<out T>(val error: Throwable, private val value: T? = null) : AsyncResult<T>(complete = true, shouldLoad = true, value = value) {
    override fun equals(other: Any?): Boolean {
        if (other !is Failure<*>) return false

        val otherError = other.error
        return error::class == otherError::class &&
                error.message == otherError.message &&
                error.stackTrace.firstOrNull() == otherError.stackTrace.firstOrNull()
    }

    override fun hashCode(): Int = arrayOf(error::class, error.message, error.stackTrace[0]).contentHashCode()
}

/**
 * [AsyncResult] Uninitialized state.
 */
object Uninitialized : AsyncResult<Nothing>(complete = false, shouldLoad = true, value = null)