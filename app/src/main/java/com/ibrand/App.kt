package com.ibrand

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    init { AppCompatDelegate.setCompatVectorFromResourcesEnabled(true) }
    override fun onCreate() {
        super.onCreate()
        setAppLocale()
        FirebaseApp.initializeApp(applicationContext)
    }

    private fun setAppLocale() {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
    }

}