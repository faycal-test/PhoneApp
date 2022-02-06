package com.appfiza.phoneapp

import android.app.Application
import com.appfiza.phoneapp.data.remote.api.ApiConstants
import com.appfiza.phoneapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by Fayçal KADDOURI 🐈
 */
open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    open fun setupKoin() {
        startKoin {
            androidContext(applicationContext)
            modules(
                networkModule(ApiConstants.BASE_URL),
                roomModule,
                mappersModule,
                useCasesModule,
                viewModelModule,
                repositoryModule,
                )
        }
    }

}