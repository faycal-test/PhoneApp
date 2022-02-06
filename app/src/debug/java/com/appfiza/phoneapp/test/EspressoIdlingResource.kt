package com.appfiza.phoneapp.test

import androidx.test.espresso.IdlingResource

/**
 * Created by Fayçal KADDOURI 🐈
 */
object EspressoIdlingResource {

    private const val resource = "GLOBAL"
    private val countingIdlingResource = SimpleCountingIdlingResource(resource)

    fun increment() = countingIdlingResource.increment()

    fun decrement() = countingIdlingResource.decrement()

    fun getIdlingResource(): IdlingResource = countingIdlingResource

}