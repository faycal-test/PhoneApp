package com.appfiza.phoneapp.model

/**
 * Created by Fayçal KADDOURI 🐈
 */
data class Device(
    val id: Int,
    val manufacturer: String,
    val isFavorite: Boolean,
    val model: String,
    val image: String,
    val price: String,
    val description: String,
    val specs: Specs,
    val stock: Int
)