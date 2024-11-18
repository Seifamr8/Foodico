package com.example.fa.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.fa.R
import com.example.fa.database.MealDataBase
import com.example.fa.fragments.HomeFragment
import com.example.fa.viewModel.HomeViewModel
import com.example.fa.viewModel.HomeViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val viewModel:HomeViewModel by lazy {
        val mealDatabase= MealDataBase.getInstance(this)
        val homeViewModelProviderFactory = HomeViewModelFactory(mealDatabase)
        ViewModelProvider(this,homeViewModelProviderFactory)[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

    }
    override fun onStop() {
        super.onStop()
        val sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", false) // Clear login state
        editor.apply()
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.homeFragment, fragment)
        transaction.commit()
    }
}