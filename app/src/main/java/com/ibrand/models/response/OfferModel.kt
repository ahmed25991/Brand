package com.ibrand.models.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class OfferModel(
    @SerializedName("id")
    @Expose
    var id: Int?,
    @SerializedName("image")
    @Expose
    var image: String?,
    @SerializedName("image_small")
    @Expose
    var imageSmall: String?,
    @SerializedName("is_active")
    @Expose
    var isActive: Boolean?,
    @SerializedName("name")
    @Expose
    var name: String?,
    @SerializedName("notes")
    @Expose
    var notes: String?,
    @SerializedName("products")
    @Expose
    var products: List<ProductModel>?
):Parcelable