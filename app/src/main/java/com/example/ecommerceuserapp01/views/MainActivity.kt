package com.example.ecommerceuserapp01.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.ecommerceuserapp01.R
import com.example.ecommerceuserapp01.viewmodels.LoginViewModel
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Timestamp
import java.util.*

class MainActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels() //we are in the activity so only viewmodel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.mToolbar)
        setSupportActionBar(toolbar)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        val drawer: DrawerLayout = findViewById(R.id.main_drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawer)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    //so we can log out by onDestroy() or onStop()
    override fun onStop() {
        super.onStop()
        //So I have to check one thing that user is loged or not
       //If current user is not equal null then let method will call
        loginViewModel.auth.currentUser?.let{
         loginViewModel.updateAppExitTimeAndAvailableStatus(
             status = false,
             time = Timestamp(Date(System.currentTimeMillis()))
         )
       }
    }

    override fun onResume() {
        super.onResume()
        //first we check user is authenticate or not
        loginViewModel.auth.currentUser?.let{
            //If authenticated
            loginViewModel.updateAvailableStatus(true)
        }
    }

    //Last case is logout

    //For logout we do not create method we just call same method from
    //loginview model




}