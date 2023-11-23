package com.ibrand.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ibrand.datasource.service.RemoteArrayResponse
import com.ibrand.datasource.service.RemoteResponse
import com.ibrand.models.MainResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

abstract class BaseRepository {

    protected fun <T : Any> buildApi(task: suspend () -> RemoteResponse<T>) =
        flow {
            val response = task()

            when {
                response.isSuccessful -> {
                    val baseBody = response.body()
                    if (baseBody != null) {
                        emit(BaseResponse.Success(data = baseBody))
                    } else {
                        emit(BaseResponse.Error(throwable = response.message()))
                    }
                }

                else -> {
                    val responseBody = response.errorBody()
                    responseBody?.let {
                        val gson = Gson()
                        val type = object : TypeToken<MainResponse>() {}.type
                        val errorResponse: MainResponse? =
                            gson.fromJson(response.errorBody()!!.charStream(), type)
                        emit(BaseResponse.Error(throwable = errorResponse?.message!!))
                    }

                }
            }
        }
            .onStart { emit(BaseResponse.Loading(loading = true)) }
            .catch { throwable ->
                emit(BaseResponse.Error(throwable = throwable.message.toString()))
            }


    protected fun <T : Any> buildListApi(task: suspend () -> RemoteArrayResponse<T>) =
        flow {
            val response = task()

            when {
                response.isSuccessful -> {
                    val baseBody = response.body()
                    if (baseBody != null) {
                        emit(BaseResponse.Success(data = baseBody))
                    } else {
                        emit(BaseResponse.Error(throwable = response.message()))
                    }
                }

                else -> {
                    val responseBody = response.errorBody()
                    responseBody?.let {
                        val gson = Gson()
                        val type = object : TypeToken<MainResponse>() {}.type
                        val errorResponse: MainResponse? =
                            gson.fromJson(response.errorBody()!!.charStream(), type)
                        emit(BaseResponse.Error(throwable = errorResponse?.message!!))
                    }

                }
            }
        }
            .onStart { emit(BaseResponse.Loading(loading = true)) }
            .catch { throwable ->
                emit(BaseResponse.Error(throwable = throwable.message.toString()))
            }


}