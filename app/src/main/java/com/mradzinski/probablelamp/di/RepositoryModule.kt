package com.mradzinski.probablelamp.di

import com.mradzinski.probablelamp.data.repository.HomeRepository
import com.mradzinski.probablelamp.data.repository.HomeRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<HomeRepository> { HomeRepositoryImpl(get()) }

}