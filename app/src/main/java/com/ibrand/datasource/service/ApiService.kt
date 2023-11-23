package com.ibrand.datasource.service

import com.ibrand.datasource.service.NetworkConstants.categories
import com.ibrand.datasource.service.NetworkConstants.countries
import com.ibrand.datasource.service.NetworkConstants.products
import com.ibrand.datasource.service.NetworkConstants.sliderOffers
import com.ibrand.models.BaseBody
import com.ibrand.models.BaseListBody
import com.ibrand.models.response.CategoryModel
import com.ibrand.models.response.CountryModel
import com.ibrand.models.response.OfferModel
import com.ibrand.models.response.ProductModel
import com.ibrand.models.response.UserModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

typealias RemoteResponse<T> = Response<BaseBody<T>>
typealias RemoteArrayResponse<E> = Response<BaseListBody<E>>


interface ApiService {
    @GET(value = countries)
    suspend fun getCountries(): RemoteArrayResponse<List<CountryModel>>

    @POST
    @FormUrlEncoded
    suspend fun login(@Field("mobile") phoneNumber:String, @Field("password") password:String, @Url url:String): RemoteResponse<UserModel>

    @GET(value = categories)
    suspend fun displayCategories(): RemoteArrayResponse<List<CategoryModel>>

    @GET(value = sliderOffers)
    suspend fun displaySliderOffers(@Query("section")isSlider:Int): RemoteArrayResponse<List<OfferModel>>


    @GET(value = sliderOffers)
    suspend fun displayPromotions(@Query("section")isSlider:Int): RemoteArrayResponse<List<OfferModel>>

    @GET(value = products)
    suspend fun displayProducts(@Query("productcategory__category__in")categoyId:Int): RemoteArrayResponse<List<ProductModel>>

    @GET(value = products)
    suspend fun displayProducts(): RemoteArrayResponse<List<ProductModel>>
}