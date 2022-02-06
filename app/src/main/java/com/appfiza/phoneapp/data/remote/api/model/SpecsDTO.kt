package com.appfiza.phoneapp.data.remote.api.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Fayçal KADDOURI 🐈
 */
data class SpecsDTO (
    @SerializedName("body") val body : String?,
    @SerializedName("memory") val memory : String?,
    @SerializedName("battery") val battery : String?,
    @SerializedName("chipset") val chipset : String?,
    @SerializedName("display") val display : String?,
    @SerializedName("platform") val platform : String?
)