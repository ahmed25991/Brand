package com.ibrand.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ibrand.R
import com.ibrand.base.BaseFragment
import com.ibrand.databinding.FragmentSplashBinding
import com.ibrand.ui.MainActivity
import com.ibrand.utils.Constants.IS_FIRST_TIME_KEY
import com.ibrand.utils.Constants.USER_TOKEN
import com.ibrand.utils.NavigationUtil.navigateTo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash) {

    var splashHandler = Handler(Looper.getMainLooper())
    private fun start() { splashHandler.postDelayed(splashRunnable, 2000) }
    private fun stop() { splashHandler.removeCallbacks(splashRunnable) }

    private var splashRunnable = Runnable {
        lifecycleScope.launch {
            when {
                prefs.retrieve<Boolean>(IS_FIRST_TIME_KEY) == null || prefs.retrieve<Boolean>(IS_FIRST_TIME_KEY) == true -> { this@SplashFragment.navigateTo(R.id.splash_to_general_settings_fragment) }
                else -> {
                    if (prefs.retrieve<String>(USER_TOKEN).isNullOrEmpty()) { this@SplashFragment.navigateTo(R.id.splash_to_login_fragment) }
                    else {
                        startActivity(Intent(requireContext(),MainActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
            repeatOnLifecycle(Lifecycle.State.RESUMED) { start() }

        }
    }


    override fun onStop() {
        super.onStop()
        stop()
    }

    override fun onResume() {
        super.onResume()
        start()
    }

}