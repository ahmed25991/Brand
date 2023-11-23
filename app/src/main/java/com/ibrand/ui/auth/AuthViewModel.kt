package com.ibrand.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.ibrand.base.BaseResponse
import com.ibrand.base.BaseViewModel
import com.ibrand.models.BaseBody
import com.ibrand.models.BaseListBody
import com.ibrand.models.response.CountryModel
import com.ibrand.models.response.UserModel
import com.ibrand.repos.MainRepo
import com.ibrand.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: MainRepo) : BaseViewModel() {

    val countriesResponse = Resource<BaseListBody<List<CountryModel>>>(viewModelScope)
    val countriesLoading: LiveData<Boolean> = countriesResponse.liveState.map { it is BaseResponse.Loading }

    fun displayCountries() = countriesResponse.fetch(repo.displayCountries(), Resource.FetchType.INVALIDATION)


    val loginResponse = Resource<BaseBody<UserModel>>(viewModelScope)
    val loginLoading: LiveData<Boolean> = loginResponse.liveState.map { it is BaseResponse.Loading }

    fun login(phoneNumber:String,password:String,url:String) =
        loginResponse.fetch(repo.login(phoneNumber =phoneNumber,password=password,url=url), Resource.FetchType.INVALIDATION)


}





