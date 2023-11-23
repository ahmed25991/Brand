package com.ibrand.datasource.interceptor

import com.ibrand.sharedPref.UserPreferenceHelper
import com.ibrand.utils.Constants.USER_TOKEN
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(private val pref: UserPreferenceHelper) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder: Request.Builder =

            if (pref.retrieve<String>(USER_TOKEN).isNullOrEmpty()) {
                original.newBuilder()
            } else {
                original.newBuilder().addHeader("Authorization", "Token " + pref.retrieve<String>(USER_TOKEN)!!)
            }






        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}