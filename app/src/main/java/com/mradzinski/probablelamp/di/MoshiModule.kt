package com.mradzinski.probablelamp.di

import com.squareup.moshi.Moshi
import org.koin.dsl.module

val moshiModule = module {

    single { Moshi.Builder().build() }

}