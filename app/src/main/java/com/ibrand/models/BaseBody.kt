package com.ibrand.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class BaseBody<T>(
    @Expose
    @SerializedName("status")
    val status: Boolean,
    @Expose
    @SerializedName("message")
    val message: String,
    @Expose
    @SerializedName("data")
    val data: T? = null)
