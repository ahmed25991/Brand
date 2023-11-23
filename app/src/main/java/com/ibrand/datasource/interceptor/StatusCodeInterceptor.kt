package com.ibrand.datasource.interceptor

import com.ibrand.sharedPref.UserPreferenceHelper
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class StatusCodeInterceptor @Inject constructor(pref: UserPreferenceHelper) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}