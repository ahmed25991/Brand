package com.ibrand.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class WalkThroughResponse(
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("image")
    val image: Int,
    @Expose
    @SerializedName("title")
    var title: String? = null,
    @Expose
    @SerializedName("description")
    var description: String? = null
)
