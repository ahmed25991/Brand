package com.ibrand.models.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CountryModel(
    @Expose
    @SerializedName("currency")
    val currency: String,
    @Expose
    @SerializedName("currency_symbol")
    val currencySymbol: String,
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("image")
    val image: Image,
    @Expose
    @SerializedName("iso")
    val iso: String,
    @Expose
    @SerializedName("iso3")
    val iso3: String,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("phone_code")
    val phoneCode: String
):Parcelable