package com.ibrand.models.response



import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Price(
    @SerializedName("currency")
    @Expose
    var currency: String?,
    @SerializedName("excl_tax")
    @Expose
    var exclTax: Double?,
    @SerializedName("incl_tax")
    @Expose
    var inclTax: Double?,
    @SerializedName("tax")
    @Expose
    var tax: Double?
):Parcelable