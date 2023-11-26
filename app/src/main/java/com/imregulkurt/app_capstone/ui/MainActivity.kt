package com.imregulkurt.app_capstone.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.imregulkurt.app_capstone.R
import com.imregulkurt.app_capstone.common.gone
import com.imregulkurt.app_capstone.common.viewBinding
import com.imregulkurt.app_capstone.common.visible
import com.imregulkurt.app_capstone.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private lateinit var navController: NavController

    private val myListener = NavController.OnDestinationChangedListener {_, destination, _ ->
        changeBottomVisibility(destination.id)
    }

    private fun changeBottomVisibility(destinationId: Int) {
        if (destinationId == R.id.homeFragment || destinationId == R.id.cartFragment ||
            destinationId == R.id.favoriteFragment || destinationId == R.id.searchFragment ||
            destinationId == R.id.profileFragment){
            binding.bottomNavigationView.visible()
        } else {
            binding.bottomNavigationView.gone()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        navController.addOnDestinationChangedListener(myListener)
    }

    // TODO: Ask whether this is necessary (to unsubscribe on destroy)
    // override fun onDestroy() {
    //     super.onDestroy()
    //     navController.removeOnDestinationChangedListener(myListener)
    // }
}
