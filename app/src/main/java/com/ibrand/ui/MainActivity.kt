package com.ibrand.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.ibrand.utils.Constants
import com.ibrand.R
import com.ibrand.base.BaseActivity
import com.ibrand.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    lateinit var graph: NavGraph
    var isGoTopROFILEdIRECT = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        isGoTopROFILEdIRECT = intent.extras?.getBoolean("GOTO_EDIT_PROFILE_PAGE")?:false

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_main_nav_host) as NavHostFragment
        val navController = navHostFragment.navController
        val inflater = navHostFragment.navController.navInflater
        graph = inflater.inflate(R.navigation.graph_main)
        if (isGoTopROFILEdIRECT){ graph.setStartDestination(R.id.fragmentCompleteProfile) }else { graph.setStartDestination(R.id.fragmentHome) }
        navController.graph = graph

        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
            navController.popBackStack(item.itemId, inclusive = false)
            true
        }


        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.fragmentCategories || destination.id == R.id.fragmentBrands ||
                destination.id == R.id.fragmentHome || destination.id == R.id.fragmentCart ||
                destination.id == R.id.fragmentMyAccount ){
                binding.bottomNav.visibility = VISIBLE
                binding.buttonHomeNavigation.visibility = VISIBLE
                binding.ivClickCategories.visibility = VISIBLE
            }else{
                binding.bottomNav.visibility = GONE
                binding.buttonHomeNavigation.visibility = GONE
                binding.ivClickCategories.visibility = GONE
            }
        }
    }


    override fun statusOfInternetChangedToConnected(status: String) {}
}
