package com.ibrand.base

sealed class BaseResponse<out T : Any> {
    data class Success<out T : Any>(val data: T) : BaseResponse<T>()
    data class Error(val throwable: String) : BaseResponse<Nothing>()
    data class Loading(val loading: Boolean) : BaseResponse<Nothing>()
    object None : BaseResponse<Nothing>()

    val dataOrNull = if (this is Success) data else null

    val isSuccess get() = this is Success
    val isError get() = this is Error
    val isLoading get() = this is Loading
}