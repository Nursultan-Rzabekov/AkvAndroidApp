package com.example.akvandroidapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.akvandroidapp.R
import com.example.akvandroidapp.entity.AuthToken
import com.example.akvandroidapp.ui.BaseActivity
import com.example.akvandroidapp.ui.auth.AuthActivity
import com.example.akvandroidapp.ui.main.favorite.BaseFavoriteFragment
import com.example.akvandroidapp.ui.main.home.BaseHomeFragment
import com.example.akvandroidapp.ui.main.messages.BaseMessagesFragment
import com.example.akvandroidapp.ui.main.profile.BaseProfileFragment
import com.example.akvandroidapp.ui.main.profile.add_ad.*
import com.example.akvandroidapp.ui.main.search.ApartmentsFragment
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.ui.main.search.filter.FilterCityFragment
import com.example.akvandroidapp.ui.main.search.filter.FilterTypeFragment
import com.example.akvandroidapp.ui.main.search.filter.FilterUdopstvaFragment
import com.example.akvandroidapp.ui.main.search.filter.SearchFilterFragment
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeFragment
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeReviewFragment
import com.example.akvandroidapp.ui.main.search.zhilye.ZhilyeRulesOfHouseFragment
import com.example.akvandroidapp.util.BottomNavController
import com.example.akvandroidapp.util.setUpNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.explore_active.*


class MainActivity : BaseActivity(),
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

        is ProfileAddAddressFragment -> {
            navController.navigate(R.id.action_profileAddAddressFragment_to_home)
        }

        is ProfileAddCheckFragment -> {
            navController.navigate(R.id.action_profileAddCheckFragment_to_home)
        }

        is ProfileAddDescriptionFragment -> {
            navController.navigate(R.id.action_profileAddDescriptionFragment_to_home)
        }

        is ProfileAddGalleryFragment -> {
            navController.navigate(R.id.action_profileAddGalleryFragment_to_home)
        }

        is ProfileAddNearFragment -> {
            navController.navigate(R.id.action_profileAddNearFragment_to_home)
        }

        is ProfileAddPriceFragment -> {
            navController.navigate(R.id.action_profileAddPriceFragment_to_home)
        }

        is ProfileAddQuestsFragment -> {
            navController.navigate(R.id.action_profileAddQuestsFragment_to_home)
        }

        is ProfileAddRequirementFragment -> {
            navController.navigate(R.id.action_profileAddRequirementFragment_to_home)
        }

        is ProfileAddRulesFragment -> {
            navController.navigate(R.id.action_profileAddRulesFragment_to_home)
        }

        is ProfileAddTypeFragment -> {
            navController.navigate(R.id.action_profileAddTypeFragment_to_home)
        }

        is ZhilyeFragment -> {
            navController.navigate(R.id.action_zhilyeFragment_to_home)
        }

        is ZhilyeReviewFragment -> {
            navController.navigate(R.id.action_zhilyeReviewFragment_to_home)
        }

        is ZhilyeRulesOfHouseFragment -> {
            navController.navigate(R.id.action_zhilyeRulesOfHouseFragment_to_home)
        }

        is FilterCityFragment -> {
            navController.navigate(R.id.action_filterCityFragment_to_home)
        }

        is FilterTypeFragment -> {
            navController.navigate(R.id.action_filterTypeFragment_to_home)
        }

        is FilterUdopstvaFragment -> {
            navController.navigate(R.id.action_filterUdopstvaFragment_to_home)
        }

        is SearchFilterFragment -> {
            navController.navigate(R.id.action_searchFilterFragment_to_home)
        }

        is ApartmentsFragment -> {
            navController.navigate(R.id.action_apartmentsFragment_to_home)
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

        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }

        sessionManager.login(AuthToken(1,"305f6398506c603c7b811cd48c039d9d5e4687ea"))
        subscribeObservers()

    }


    fun subscribeObservers(){
        sessionManager.cachedToken.observe(this, Observer{ authToken ->
            Log.d(TAG, "MainActivity, subscribeObservers: ViewState: ${authToken}")
            if(authToken.token == null){
                navAuthActivity()
                finish()
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
