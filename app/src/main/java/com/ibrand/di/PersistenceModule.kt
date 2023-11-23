package com.ibrand.di

import android.content.Context
import com.ibrand.sharedPref.UserPreferenceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

    @Singleton
    @Provides
    fun providePref(@ApplicationContext context: Context): UserPreferenceHelper = UserPreferenceHelper(application = context)

}
