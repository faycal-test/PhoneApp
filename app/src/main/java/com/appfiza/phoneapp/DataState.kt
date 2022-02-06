package com.appfiza.phoneapp

/**
 * Created by Fayçal KADDOURI 🐈
 */
sealed class DataState<out T> {
    object Idle : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    object Error : DataState<Nothing>()
    object Exception : DataState<Nothing>()
    data class Success<out T>(val data: T) : DataState<T>()
}