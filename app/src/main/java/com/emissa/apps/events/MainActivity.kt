package com.emissa.apps.events

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.emissa.apps.events.databinding.ActivityMainBinding
import com.emissa.apps.events.views.EventFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    companion object {
        private const val LOG_TAG = "MainActivity"
    }
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.main_frag_container
        ) as NavHostFragment

        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.events_fragment, R.id.create_events, R.id.event_details)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        fragmentNavigation(supportFragmentManager, EventFragment())
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}