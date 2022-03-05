package com.mradzinski.probablelamp.data.interactor.base

import kotlinx.coroutines.flow.Flow
import java.util.*

interface FlowInteractor <T : Any, in P> : BaseInteractor  {

    override val name: String
        get() = UUID.randomUUID().toString()

    /**
     * Builds an [Observable] which will be used when executing the current [FlowInteractor].
     */
    fun build(params: P): Flow<T>

}