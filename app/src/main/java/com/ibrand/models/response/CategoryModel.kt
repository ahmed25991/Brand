package com.ibrand.models.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryModel(
    @SerializedName("breadcrumbs")
    @Expose
    var breadcrumbs: String?=null,
    @SerializedName("childrens")
    @Expose
    var childrens: List<CategoryModel?>?= emptyList(),
    @SerializedName("description")
    @Expose
    var description: String?=null,
    @SerializedName("get_num_children")
    @Expose
    var getNumChildren: Int?=null,
    @SerializedName("id")
    @Expose
    var id: Int?=null,
    @SerializedName("image")
    @Expose
    var image: String?=null,
    @SerializedName("meta_description")
    @Expose
    var metaDescription: String?=null,
    @SerializedName("meta_title")
    @Expose
    var metaTitle: String?=null,
    @SerializedName("name")
    @Expose
    var name: String?=null,
    @SerializedName("url")
    @Expose
    var url: String?=null
):Parcelable
