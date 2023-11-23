package com.ibrand.models.response



import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductModel(
    @SerializedName("id")
    @Expose
    var id: Int?,
    @SerializedName("images")
    @Expose
    var images: List<ProductImageModel>?,
    @SerializedName("price")
    @Expose
    var price: Price?,
    @SerializedName("title")
    @Expose
    var title: String?,
    @SerializedName("url")
    @Expose
    var url: String?
):Parcelable