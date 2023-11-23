package com.ibrand.di

import android.content.Context
import com.ibrand.BuildConfig
import com.ibrand.datasource.interceptor.HeaderInterceptor
import com.ibrand.datasource.interceptor.StatusCodeInterceptor
import com.ibrand.datasource.service.NetworkConstants
import com.ibrand.datasource.service.NetworkConstants.CACHE_SIZE
import com.ibrand.sharedPref.UserPreferenceHelper
import com.ibrand.utils.ValidateSSL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache =
        Cache(context.cacheDir, CACHE_SIZE)

    //
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) { HttpLoggingInterceptor.Level.BODY } else { HttpLoggingInterceptor.Level.NONE }
    }

    //
    @Provides
    @Singleton
    fun provideHeaderInterceptor(pref: UserPreferenceHelper): HeaderInterceptor = HeaderInterceptor(pref)

    //
    @Provides
    @Singleton
    fun provideStatusCodeInterceptor(pref: UserPreferenceHelper): StatusCodeInterceptor = StatusCodeInterceptor(pref)

    //
    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache,
        headerInterceptor: HeaderInterceptor,
        statusCodeInterceptor: StatusCodeInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {

        return ValidateSSL.getUnsafeOkHttpClient().newBuilder().apply {
            connectTimeout(NetworkConstants.timeOut, TimeUnit.SECONDS)
            readTimeout(NetworkConstants.timeOut, TimeUnit.SECONDS)
            writeTimeout(NetworkConstants.timeOut, TimeUnit.SECONDS)
            addInterceptor(headerInterceptor)
            if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
            cache(cache)
        }.build()
    }

    //
    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    //
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient, converterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder().client(okHttpClient).baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(converterFactory).build()

}
