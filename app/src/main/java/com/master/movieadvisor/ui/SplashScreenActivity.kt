package com.master.movieadvisor.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.master.movieadvisor.HomeActivity
import com.master.movieadvisor.MainActivity
import com.master.movieadvisor.PreferenceHelper
import com.master.movieadvisor.R



class SplashScreenActivity: AppCompatActivity() {
    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var mLogo : ImageView
    private lateinit var auth: FirebaseAuth
    private val timeout: Long =2500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeStatusBarTransparent()
        setContentView(R.layout.splash_screen)
        init()
        animateLogo()
        val currentUser = auth.currentUser
        Handler().postDelayed({
            currentUser?.also{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                return@postDelayed
            }
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

        }, timeout)

    }


    private fun init(){
        mLogo = findViewById(R.id.animLogo)
        auth = Firebase.auth
    }

    private fun animateLogo(){
        val animation: Animation= AnimationUtils.loadAnimation(this,
            R.anim.anim
        )
        mLogo.startAnimation(animation)
    }

}