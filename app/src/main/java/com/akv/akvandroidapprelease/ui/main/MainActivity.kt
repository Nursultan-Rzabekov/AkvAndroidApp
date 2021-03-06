package com.akv.akvandroidapprelease.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.akv.akvandroidapprelease.R
import com.akv.akvandroidapprelease.ui.BaseLocationActivity
import com.akv.akvandroidapprelease.ui.auth.AuthActivity
import com.akv.akvandroidapprelease.ui.main.favorite.BaseFavoriteFragment
import com.akv.akvandroidapprelease.ui.main.favorite.FavoriteFragment
import com.akv.akvandroidapprelease.ui.main.home.BaseHomeFragment
import com.akv.akvandroidapprelease.ui.main.home.PayBoxPayWebViewFragment
import com.akv.akvandroidapprelease.ui.main.messages.BaseMessagesFragment
import com.akv.akvandroidapprelease.ui.main.profile.AboutProfileFragment
import com.akv.akvandroidapprelease.ui.main.profile.BaseProfileFragment
import com.akv.akvandroidapprelease.ui.main.profile.account.AccountUserEditProfileFragment
import com.akv.akvandroidapprelease.ui.main.profile.account.AccountUserProfileFragment
import com.akv.akvandroidapprelease.ui.main.profile.settings.SettingsGeolocationProfileFragment
import com.akv.akvandroidapprelease.ui.main.profile.settings.SettingsProfileFragment
import com.akv.akvandroidapprelease.ui.main.profile.settings.SettingsRegionProfileFragment
import com.akv.akvandroidapprelease.ui.main.search.ApartmentsFragment
import com.akv.akvandroidapprelease.ui.main.search.BaseSearchFragment
import com.akv.akvandroidapprelease.util.BottomNavController
import com.akv.akvandroidapprelease.util.setUpNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.explore_active.*
import java.util.*


class MainActivity : BaseLocationActivity(),
    BottomNavController.NavGraphProvider,
    BottomNavController.OnNavigationGraphChanged,
    BottomNavController.OnNavigationReselectedListener
{

    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.nav_search,
            this,
            this)
    }


    override fun getNavGraphId(itemId: Int) = when(itemId){
        R.id.nav_search -> {
            R.navigation.nav_create_search
        }
        R.id.nav_home -> {
            R.navigation.nav_create_home
        }
        R.id.nav_profile -> {
            R.navigation.nav_create_profile
        }
        R.id.nav_favorite -> {
            R.navigation.nav_create_favorite
        }
        R.id.nav_messages -> {
            R.navigation.nav_create_messages
        }
        else -> {
            R.navigation.nav_create_search
        }
    }


    override fun onReselectNavItem(
        navController: NavController,
        fragment: Fragment
    ) = when(fragment){

        is ApartmentsFragment -> {
            navController.navigate(R.id.action_apartmentsFragment_to_home)
        }

        is AccountUserProfileFragment -> {
            navController.navigate(R.id.action_profileAccountUserProfileFragment_to_home)
        }

        is AccountUserEditProfileFragment -> {
            navController.navigate(R.id.action_profileAccountUserEditProfileFragment_to_home)
        }

        is AboutProfileFragment -> {
            navController.navigate(R.id.action_profileAboutProfileFragment_to_home)
        }

        is SettingsProfileFragment -> {
            navController.navigate(R.id.action_profileSettingsProfileFragment_to_home)
        }

        is SettingsRegionProfileFragment -> {
            navController.navigate(R.id.action_profileSettingsRegionProfileFragment_to_home)
        }

        is FavoriteFragment -> {
            navController.navigate(R.id.action_favoriteFragment_to_favoriteFragment)
        }

        is SettingsGeolocationProfileFragment -> {
            navController.navigate(R.id.action_profileSettingsGeolocationProfileFragment_to_home)
        }

        is PayBoxPayWebViewFragment -> {
            navController.navigate(R.id.action_payBoxPayWebViewFragment_to_home)
        }

        else -> {
            // do nothing
        }
    }


    override fun onGraphChange() {
        cancelActiveJobs()
        expandAppBar()

    }

    private fun cancelActiveJobs(){
        val fragments = bottomNavController.fragmentManager
            .findFragmentById(bottomNavController.containerId)
            ?.childFragmentManager
            ?.fragments
        if(fragments != null){
            for(fragment in fragments){
                if(fragment is BaseHomeFragment){
                    fragment.cancelActiveJobs()
                }
                if(fragment is BaseFavoriteFragment){
                    fragment.cancelActiveJobs()
                }
                if(fragment is BaseSearchFragment){
                    fragment.cancelActiveJobs()
                }
                if(fragment is BaseMessagesFragment){
                    fragment.cancelActiveJobs()
                }
                if(fragment is BaseProfileFragment){
                    fragment.cancelActiveJobs()
                }
            }
        }
        displayProgressBar(false)
    }


    override fun onBackPressed() = bottomNavController.onBackPressed()

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.explore_active)

        Locale.setDefault(Locale.forLanguageTag("ru"))

        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }

//        sessionManager.login(AuthToken(1,"305f6398506c603c7b811cd48c039d9d5e4687ea"))
        subscribeObservers()
    }


    fun subscribeObservers(){
        sessionManager.cachedToken.observe(this, Observer{ authToken ->
            Log.d(TAG, "MainActivity, subscribeObservers: ViewState: ${authToken}")
            if(authToken == null){
                navAuthActivity()
            }
        })
    }

    override fun expandAppBar() {
        //findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }


    private fun navAuthActivity(){
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun displayProgressBar(bool: Boolean){
        if(bool){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.header_zhilye_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
