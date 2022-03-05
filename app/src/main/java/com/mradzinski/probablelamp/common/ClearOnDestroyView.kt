package com.mradzinski.probablelamp.common

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private class ClearOnDestroyView<T : Any> : ReadWriteProperty<Fragment, T> {

    private var value: T? = null

    private val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            this@ClearOnDestroyView.value = null
        }
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        value ?: error(
            "Property ${property.name} should be initialized before get " +
                    "and not called after onDestroyView"
        )

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) = synchronized(this) {
        this.value = value
        thisRef.viewLifecycleOwner.lifecycle.removeObserver(observer)
        thisRef.viewLifecycleOwner.lifecycle.addObserver(observer)
    }

}

fun <T : Any> clearOnDestroyView(): ReadWriteProperty<Fragment, T> =
    ClearOnDestroyView()