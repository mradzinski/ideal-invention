package com.mradzinski.probablelamp.di

import com.mradzinski.probablelamp.data.interactor.GetCharactersByPageInteractor
import com.mradzinski.probablelamp.data.interactor.GetCharactersByPageInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<GetCharactersByPageInteractor> { GetCharactersByPageInteractorImpl(get()) }

}