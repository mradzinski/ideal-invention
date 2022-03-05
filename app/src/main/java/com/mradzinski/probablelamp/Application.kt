package com.mradzinski.probablelamp

import androidx.multidex.MultiDexApplication
import com.mradzinski.probablelamp.di.interactorModule
import com.mradzinski.probablelamp.di.moshiModule
import com.mradzinski.probablelamp.di.networkModule
import com.mradzinski.probablelamp.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level
import org.koin.core.context.startKoin

class Application : MultiDexApplication() {

    private val koinModules = listOf(
        networkModule,
        moshiModule,
        repositoryModule,
        interactorModule
    )

    override fun onCreate() {
        super.onCreate()
        startKoin()
    }

    /* ********************************************
     *               Private methods              *
     ******************************************** */

    private fun startKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@Application)
            modules(koinModules)
        }
    }
}