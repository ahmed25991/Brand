package com.ibrand.ui.home

import androidx.lifecycle.viewModelScope
import com.ibrand.base.BaseViewModel
import com.ibrand.models.BaseListBody
import com.ibrand.models.response.CategoryModel
import com.ibrand.models.response.OfferModel
import com.ibrand.models.response.ProductModel
import com.ibrand.repos.MainRepo
import com.ibrand.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: MainRepo) : BaseViewModel() {


    val categoriesResponse = Resource<BaseListBody<List<CategoryModel>>>(viewModelScope)
//    val mainCategoryLoading: LiveData<Boolean> = categoriesResponse.liveState.map { it is BaseResponse.Loading }
    fun displayCategories() = categoriesResponse.fetch(repo.displayCategories(),
    Resource.FetchType.ONETIME)

    val productsResponse = Resource<BaseListBody<List<ProductModel>>>(viewModelScope)
//    val productsLoading: LiveData<Boolean> = productsResponse.liveState.map { it is BaseResponse.Loading }
    fun displayProducts(catId:Int) = productsResponse.fetch(repo.displayProducts(catId),
        Resource.FetchType.ONETIME)

    val sliderOffersResponse = Resource<BaseListBody<List<OfferModel>>>(viewModelScope)
    // val mainOffersLoading: LiveData<Boolean> = sliderOffersResponse.liveState.map { it is BaseResponse.Loading }
    fun displaySliderOffers() = sliderOffersResponse.fetch(repo.displaySliderOffers(), Resource.FetchType.ONETIME)


    val sliderPromotionsResponse = Resource<BaseListBody<List<OfferModel>>>(viewModelScope)
    // val promotionLoading: LiveData<Boolean> = sliderPromotionsResponse.liveState.map { it is BaseResponse.Loading }
    fun displayPromotions() = sliderPromotionsResponse.fetch(repo.displayPromotions(), Resource.FetchType.ONETIME)

}





