package com.mradzinski.probablelamp.data.interactor.base

interface BaseInteractor {
    /**
     * The name of this interactor. Note that every new instance **will** hold a
     * brand new name unless [interactorName] is overwritten.
     */
    val name: String

    /**
     * Returns this interactor unique name. Note that **every new instance will have a
     * brand new name**. If a static name is required to be held among instances then the implementer
     * must overwrite this method and provide an unique name for the interactor.
     */
    fun interactorName(): String = this.name
}