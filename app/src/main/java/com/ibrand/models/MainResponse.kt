package com.ibrand.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MainResponse(
    @Expose
    @SerializedName("message")
    val message: String,
)
