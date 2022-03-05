package com.mradzinski.probablelamp.data.interactor.base

/**
 * Class backed by a [Map] used to pass parameters to an [BaseInteractor] instance.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class InteractorParams private constructor() {

    companion object {

        /**
         * Creates a new instance of an OpusInteractorParams object. Note you should
         * use the provided DSL to create new instances and avoid using this method whenever
         * possible.
         */
        fun create(): InteractorParams = InteractorParams()
    }

    private val parameters: MutableMap<String, Any?> = mutableMapOf()

    /**
     * Returns the size of this OpusInteractorParams instance.
     */
    val size: Int
        get() { return parameters.size }

    /* ********************************************
     *              End of variables              *
     ******************************************** */


    /**
     * Associates the specified value with the specified key in
     * this OpusInteractorParams instance.
     */
    fun put(key: String, value: Any?) = parameters.put(key, value)

    /**
     * Returns the value corresponding to the given key, or defaultValue if such a key is
     * not present in this OpusInteractorParams instance.
     */
    fun getInt(key: String, defaultValue: Int = 0): Int {
        val obj = parameters[key] ?: return defaultValue
        return try {
            obj as Int
        } catch (e: ClassCastException) {
            e.printStackTrace()
            defaultValue
        }
    }

    /**
     * Returns the value corresponding to the given key, or defaultValue if such a key is
     * not present in this OpusInteractorParams instance.
     */
    fun getString(key: String, defaultValue: String = ""): String {
        val obj = parameters[key] ?: return defaultValue
        return try {
            obj as String
        } catch (e: ClassCastException) {
            e.printStackTrace()
            defaultValue
        }
    }

    /**
     * Returns the value corresponding to the given key, or defaultValue if such a key is
     * not present in this OpusInteractorParams instance.
     */
    fun getLong(key: String, defaultValue: Long = 0L): Long {
        val obj = parameters[key] ?: return defaultValue
        return try {
            obj as Long
        } catch (e: ClassCastException) {
            e.printStackTrace()
            defaultValue
        }
    }

    /**
     * Returns the value corresponding to the given key, or defaultValue if such a key is
     * not present in this OpusInteractorParams instance.
     */
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        val obj = parameters[key] ?: return defaultValue
        return try {
            obj as? Boolean ?: throw ClassCastException()
        } catch (e: ClassCastException) {
            e.printStackTrace()
            defaultValue
        }
    }

    /**
     * Returns the value corresponding to the given key, or defaultValue if such a key is
     * not present in this OpusInteractorParams instance.
     */
    fun getFloat(key: String, defaultValue: Float = 0F): Float {
        val obj = parameters[key] ?: return defaultValue
        return try {
            obj as? Float ?: throw ClassCastException()
        } catch (e: ClassCastException) {
            e.printStackTrace()
            defaultValue
        }
    }

    /**
     * Returns the value corresponding to the given key, or defaultValue if such a key is
     * not present in this OpusInteractorParams instance.
     */
    fun getDouble(key: String, defaultValue: Double = 0.0): Double {
        val obj = parameters[key] ?: return defaultValue
        return try {
            obj as? Double ?: throw ClassCastException()
        } catch (e: ClassCastException) {
            e.printStackTrace()
            defaultValue
        }
    }

    /**
     * Returns the value corresponding to the given key, or defaultValue if such a key is
     * not present in this OpusInteractorParams instance.
     */
    fun getByte(key: String, defaultValue: Byte = 0): Byte {
        val obj = parameters[key] ?: return defaultValue
        return try {
            obj as? Byte ?: throw ClassCastException()
        } catch (e: ClassCastException) {
            e.printStackTrace()
            defaultValue
        }
    }

    /**
     * Returns the value corresponding to the given key, or defaultValue if such a key is
     * not present in this OpusInteractorParams instance.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getObject(key: String): T? {
        val obj = parameters[key] ?: return null
        return try {
            obj as T
        } catch (e: ClassCastException) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Removes the specified key and its corresponding value from this OpusInteractorParams.
     */
    fun remove(key: String) = parameters.remove(key)

    /**
     * Removes all elements from this OpusInteractorParams.
     */
    fun clear() = parameters.clear()

    override fun toString(): String = parameters.toString()


    /* ********************************************
     *              Extension methods              *
     ******************************************** */

    /**
     * Associates this value with the specified key in this OpusInteractorParams.
     */
    fun Any?.put(key: String) { put(key, this) }


    /* ********************************************
     *              Operator methods              *
     ******************************************** */

    operator fun String.plusAssign(value: Any) = value.put(this)

    operator fun plusAssign(keyValuePair: Pair<String, Any>) = keyValuePair.second.put(keyValuePair.first)

    operator fun minus(key: String) = remove(key)

    /**
     * Returns true if this OpusInteractorParams instance contains the specified key.
     */
    operator fun contains(key: String) = parameters.containsKey(key)

}

/**
 * Creates a new [InteractorParams] instance through its DSL.
 */
inline fun interactorParams(params: InteractorParams.() -> Unit) = InteractorParams.create().apply(params)