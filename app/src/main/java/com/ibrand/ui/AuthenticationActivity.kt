package com.ibrand.ui

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.ibrand.R
import com.ibrand.base.BaseActivity
import com.ibrand.databinding.ActivityAuthBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AuthenticationActivity : BaseActivity<ActivityAuthBinding>(R.layout.activity_auth) {

    lateinit var graph: NavGraph
    lateinit var navController: NavController




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        val inflater = navHostFragment.navController.navInflater
        graph = inflater.inflate(R.navigation.graph_auth)
        graph.setStartDestination(R.id.fragmentSplash)
        navController.graph = graph
    }



    override fun statusOfInternetChangedToConnected(status: String) {
        if (status=="yes"){
            binding.fragmentNavHost.visibility = View.VISIBLE
            binding.llNoInternet.visibility = View.GONE
        }else{
            binding.fragmentNavHost.visibility = View.GONE
            binding.llNoInternet.visibility = View.VISIBLE
        }
    }





}