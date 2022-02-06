package com.appfiza.phoneapp

import androidx.test.platform.app.InstrumentationRegistry
import com.appfiza.phoneapp.App
import com.appfiza.phoneapp.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Created by Fayçal KADDOURI 🐈
 */
class AppTest: App() {

    private companion object {
        const val BASE_URL = "http://127.0.0.1:8080"
    }

    override fun setupKoin() {
        startKoin {
            androidContext(applicationContext)
            modules(
                networkModule(BASE_URL),
                roomModule,
                mappersModule,
                useCasesModule,
                viewModelModule,
                repositoryModule,
            )
        }
    }
}