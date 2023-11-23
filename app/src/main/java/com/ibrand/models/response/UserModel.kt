package com.ibrand.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserModel(
    @Expose
    @SerializedName("token")
    val token: String,
    @Expose
    @SerializedName("username")
    val username: String
)