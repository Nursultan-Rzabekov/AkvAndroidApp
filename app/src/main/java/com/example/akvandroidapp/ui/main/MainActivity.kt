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
import com.example.akvandroidapp.ui.main.search.BaseSearchFragment
import com.example.akvandroidapp.ui.main.search.SearchFilterFragment
import com.example.akvandroidapp.util.BottomNavController
import com.example.akvandroidapp.util.setUpNavigation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


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


    override fun onGraphChange() {
        //changeAppbar()
        cancelActiveJobs()
        expandAppBar()

    }

//    private fun changeAppbar(){
//
////        if(bottomNavController.onNavigationItemSelected()){
////            head_zhilye.visibility = View.GONE
////            searcher_base_layout_in.visibility = View.GONE
////            rules_of_house_in.visibility = View.GONE
////            header_profile_guest.visibility = View.VISIBLE
////        }
//
//        val fragments = bottomNavController.fragmentManager
//            .findFragmentById(bottomNavController.containerId)
//            ?.childFragmentManager
//            ?.fragments
//        if(fragments != null){
//            for(fragment in fragments){
//                if(fragment is BaseHomeFragment){
//                    head_zhilye.visibility = View.GONE
//                    searcher_base_layout_in.visibility = View.VISIBLE
//                    rules_of_house_in.visibility = View.GONE
//                    header_profile_guest.visibility = View.GONE
//                }
//                if(fragment is BaseFavoriteFragment){
//                    head_zhilye.visibility = View.GONE
//                    searcher_base_layout_in.visibility = View.VISIBLE
//                    rules_of_house_in.visibility = View.GONE
//                    header_profile_guest.visibility = View.GONE
//                }
//                if(fragment is BaseSearchFragment){
//                    head_zhilye.visibility = View.GONE
//                    searcher_base_layout_in.visibility = View.VISIBLE
//                    rules_of_house_in.visibility = View.GONE
//                    header_profile_guest.visibility = View.GONE
//                }
//                if(fragment is BaseMessagesFragment){
//                    head_zhilye.visibility = View.GONE
//                    searcher_base_layout_in.visibility = View.GONE
//                    rules_of_house_in.visibility = View.VISIBLE
//                    header_profile_guest.visibility = View.GONE
//                }
//                if(fragment is BaseProfileFragment){
//                    head_zhilye.visibility = View.GONE
//                    searcher_base_layout_in.visibility = View.GONE
//                    rules_of_house_in.visibility = View.GONE
//                    header_profile_guest.visibility = View.VISIBLE
//                }
//            }
//        }
//        displayProgressBar(false)
//    }

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


    override fun onReselectNavItem(
        navController: NavController,
        fragment: Fragment
    ) = when(fragment){

        is SearchFilterFragment -> {
            navController.navigate(R.id.action_searchFragment_to_searchFilterFragment)
        }

        else -> {

        }
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
        setContentView(R.layout.activity_main)

        setupActionBar()
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }


        sessionManager.login(AuthToken(1,"qweqweqweqe"))

//        main_filter_img_btn.setOnClickListener {
//
//        }

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
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

    private fun setupActionBar(){
        setSupportActionBar(toolbar_zhilye)
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
