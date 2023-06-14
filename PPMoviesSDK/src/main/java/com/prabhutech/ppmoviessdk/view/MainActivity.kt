package com.prabhutech.ppmoviessdk.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.prabhutech.ppmoviessdk.R
import com.prabhutech.ppmoviessdk.core.constant.SDKConstant.PASSWORD_KEY
import com.prabhutech.ppmoviessdk.core.constant.SDKConstant.USERNAME_KEY
import com.prabhutech.ppmoviessdk.core.utils.NavigationRedirection
import com.prabhutech.ppmoviessdk.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    private var fragmentLabel: String? = ""

    var username: String? = ""
    var password: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback = onBackPressedCallback)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Dark mode -> Disable
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setSupportActionBar(binding.toolbarMain)

        appBarConfiguration = AppBarConfiguration.Builder(R.id.movieListFragment).build()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_main_activity) as NavHostFragment?
        if (navHostFragment != null) navController = navHostFragment.navController

        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _: NavController?, destination: NavDestination, _: Bundle? ->
            fragmentLabel = destination.label.toString()
            binding.toolbarMain.title = fragmentLabel
            this.invalidateOptionsMenu()
        }

        username = intent.getStringExtra(USERNAME_KEY)
        password = intent.getStringExtra(PASSWORD_KEY)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            NavigationRedirection.setupNavController(this, R.id.fragment_container_main_activity)
        return NavigationUI.navigateUp(
            navController,
            appBarConfiguration
        ) || super.onSupportNavigateUp()
    }

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (fragmentLabel.equals("Movies")) finish() else onSupportNavigateUp()
            }
        }
}