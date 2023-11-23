package com.ibrand.repos

import android.util.Log
import com.ibrand.base.BaseRepository
import com.ibrand.base.BaseResponse
import com.ibrand.datasource.service.ApiService
import com.ibrand.models.BaseBody
import com.ibrand.models.BaseListBody
import com.ibrand.models.response.CountryModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject


@ViewModelScoped
class MainRepo @Inject constructor(private val apiService: ApiService) : BaseRepository() {
    fun displayCountries() = buildListApi { apiService.getCountries() }
    fun login(phoneNumber:String,password:String,url:String) = buildApi { apiService.login(phoneNumber =phoneNumber,password=password,url=url) }
    fun displayCategories() = buildListApi { apiService.displayCategories() }

    fun displaySliderOffers() = buildListApi { apiService.displaySliderOffers(1) }
    fun displayPromotions() = buildListApi { apiService.displayPromotions(2) }


    fun displayProducts(catId:Int) = buildListApi { if (catId == -1) { apiService.displayProducts() } else { apiService.displayProducts(catId) } }

}