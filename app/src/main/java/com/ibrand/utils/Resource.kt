package com.ibrand.utils

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.ibrand.base.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Resource<T : Any>(private val coroutineScope: CoroutineScope) {

    enum class FetchType {

        @Suppress("unused")
        INVALIDATION,

        ONETIME,

        REFRESH,
    }

    private var fetchingJob: Job? = null

    val mutableLiveState = MutableLiveData<BaseResponse<T>>(BaseResponse.None)

    val liveState: LiveData<BaseResponse<T>> = mutableLiveState

    val liveData by lazy { liveState.map { it.dataOrNull } }

    var state
        get() = liveState.value!!
        @MainThread
        set(value) {
            mutableLiveState.value = value
        }

    val data get() = state.dataOrNull

    val isFetched
        get() = state.isSuccess

    var isFetching = false
        private set

    @Suppress("unused")
    val isRefreshing
        get() = isFetched && isFetching

    @MainThread
    fun fetch(
        useCase: Flow<BaseResponse<T>>,
        fetchType: FetchType = FetchType.ONETIME
    ): LiveData<BaseResponse<T>> {
        if (fetchType == FetchType.ONETIME && isFetched) return liveState

        isFetching = true

        fetchingJob?.cancel()
        fetchingJob = coroutineScope.launch {
            useCase.collectLatest { incomingState ->
                if (fetchType == FetchType.REFRESH && isFetched && !incomingState.isSuccess) return@collectLatest

                mutableLiveState.value = incomingState
            }
        }.also {
            it.invokeOnCompletion { isFetching = false }
        }

        return liveState
    }

    @Suppress("unused")
    @MainThread
    fun fetch(
        useCase: suspend () -> BaseResponse<T>,
        fetchType: FetchType = FetchType.ONETIME
    ): LiveData<BaseResponse<T>> {
        if (fetchType == FetchType.ONETIME && isFetched) return liveState

        isFetching = true

        fetchingJob?.cancel()
        fetchingJob = coroutineScope.launch {
            val incomingState = useCase()

            if (fetchType == FetchType.REFRESH && isFetched && !incomingState.isSuccess) return@launch

            mutableLiveState.value = incomingState
        }.also {
            it.invokeOnCompletion { isFetching = false }
        }

        return liveState
    }

    @Suppress("unused")
    @MainThread
    fun clear() {
        fetchingJob?.cancel()
        mutableLiveState.value = BaseResponse.None
    }
}