package com.appfiza.phoneapp.data.remote.api.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Fayçal KADDOURI 🐈
 */
data class DeviceDTO(
    @SerializedName("id") val id: Int?,
    @SerializedName("manufacturer") val manufacturer: String?,
    @SerializedName("is_favorite") val isFavorite: Boolean?,
    @SerializedName("model") val model: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("price") val price: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("specs") val specs: SpecsDTO?,
    @SerializedName("stock") val stock: Int?
)