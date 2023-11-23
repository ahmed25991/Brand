package com.ibrand.models.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class ProductImageModel(
    @SerializedName("caption")
    @Expose
    var caption: String?,
    @SerializedName("date_created")
    @Expose
    var dateCreated: String?,
    @SerializedName("display_order")
    @Expose
    var displayOrder: Int?,
    @SerializedName("id")
    @Expose
    var id: Int?,
    @SerializedName("original")
    @Expose
    var original: String?
):Parcelable
