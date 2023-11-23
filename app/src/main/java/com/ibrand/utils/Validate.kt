package com.ibrand.utils

import android.content.Context
import java.util.regex.Pattern
import com.google.android.gms.common.ConnectionResult

import com.google.android.gms.common.GoogleApiAvailability





object Validate {

    fun isNum(strNum: String): Boolean {
        var ret = true
        try {
            strNum.toDouble()
        } catch (e: NumberFormatException) {
            ret = false
        }
        return ret
    }



    fun isEmpty(str: String): Boolean {
        return str.isEmpty()
    }



    fun isUserName(str: String): Boolean {
        return str.isNotEmpty()
    }


    fun isMessage(str: String): Boolean {
        return str.isNotEmpty() && str.length > 12
    }


    fun isPhone(str: String): Boolean {
        return Pattern.compile("\\d+").matcher(str).matches() && str.length>=9 && str.length<16
    }

    fun isMail(str: String): Boolean {
        return str.isNotEmpty() && Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+").matcher(str).matches()
    }



    fun isPassword(textToCheck: String): Boolean {
        return textToCheck.isNotEmpty() && textToCheck.length >= 6
    }


    fun isConfirmPassword(textToConfirmPass: String, textOfPassword: String): Boolean {
        return textToConfirmPass.isNotEmpty() && textToConfirmPass.length >= 6
                && textOfPassword.equals(textToConfirmPass, false)
    }


    fun isGooglePlayServicesAvailable(context: Context): Boolean {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        return resultCode == ConnectionResult.SUCCESS
    }



}
