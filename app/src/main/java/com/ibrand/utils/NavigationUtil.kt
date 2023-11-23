package com.ibrand.utils


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.findNavController


object NavigationUtil {

    fun Fragment.navigateTo(
        id: Int,
        args: Bundle? = null,
        navOptions: NavOptions? = null,
        extras: Navigator.Extras? = null
    ) {
        safeNavigationTask { findNavController().navigate(id, args, navOptions, extras) }
    }


    fun Fragment.clearNavigateStack(destinationId: Int? = null) {
        if (destinationId != null)
            safeNavigationTask { findNavController().popBackStack(destinationId, false) }
        else
            safeNavigationTask { findNavController().popBackStack() }


    }

    private fun safeNavigationTask(task: () -> Unit) = try {
        task.invoke()
    } catch (t: Throwable) { }

}
