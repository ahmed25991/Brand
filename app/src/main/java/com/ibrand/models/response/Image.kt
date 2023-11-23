package com.ibrand.models.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image(
    @Expose
    @SerializedName("128")
    val img128: String,
    @Expose
    @SerializedName("32")
    val img32: String,
    @Expose
    @SerializedName("svg")
    val svg: String
):Parcelable