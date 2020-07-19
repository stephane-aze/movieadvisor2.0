package com.master.movieadvisor

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
import android.widget.SearchView
import androidx.annotation.RequiresApi

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.master.movieadvisor.fragments.*

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private lateinit var preferenceHelper: PreferenceHelper
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun parametersForStatusBar(){
        setWindowFlag(true)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        setWindowFlag(false)
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun setWindowFlag( b: Boolean) {
        val winParams = window.attributes
        if (b) {
            winParams.flags = winParams.flags or FLAG_TRANSLUCENT_STATUS
        } else {
            winParams.flags = winParams.flags and FLAG_TRANSLUCENT_STATUS.inv()
        }
        window.attributes = winParams
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
            item->when(item.itemId){
                R.id.item_bande_film -> {
                    replaceFragment(CategoryMovieFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.messages -> {
                    replaceFragment(HistoryMessageFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.app_bar_search -> {
                    replaceFragment(SearchFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.profiles -> {
                    replaceFragment(ProfilesFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
        return@OnNavigationItemSelectedListener false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottom_nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        parametersForStatusBar()
        replaceFragment(CategoryMovieFragment())

}
    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(
        R.menu.main_menu,
        menu
        )
        return true
    }

}
