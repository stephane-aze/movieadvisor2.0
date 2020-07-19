package com.master.movieadvisor


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity :  AppCompatActivity()  {
    private lateinit var auth: FirebaseAuth
    private lateinit var loginEmail : EditText
    private lateinit var loginPassword : EditText
    private lateinit var forgotPassword : TextView
    private lateinit var btnLogin : Button
    private var preferenceHelper: PreferenceHelper? = null
    private lateinit var mProgressBar: ProgressBar
    private val TAG = "LoginActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        forgotPassword.setOnClickListener {
            val intent = Intent(applicationContext, ForgotPasswordActivity::class.java)
            intent.putExtra("email",loginEmail.text.toString())
            startActivity(intent)
        }
        btnLogin.setOnClickListener {

                toLogin()
        }
    }


    private fun init(){
        forgotPassword = findViewById(R.id.forgotPassword)
        loginPassword = findViewById(R.id.LoginPassword)
        loginEmail = findViewById(R.id.LoginEmail)
        btnLogin = findViewById(R.id.login)
        preferenceHelper = PreferenceHelper(this)
        mProgressBar = findViewById(R.id.progressBar)
        auth = Firebase.auth

    }


    private fun toLogin() {
        showSimpleProgressDialog()
        auth.signInWithEmailAndPassword(loginEmail.text.toString(), loginPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                removeSimpleProgressDialog()
                if (task.isSuccessful) {
                    Toast.makeText(this@LoginActivity, "L'authentification est réussi", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    displayError(task)
                }

                // ...
            }

    }

    private fun displayError(task: Task<AuthResult>) {
        // If sign in fails, display a message to the user.
        Log.w(TAG, "Authentication:failure", task.exception)
        Toast.makeText(
            baseContext, "Authentication failed.",
            Toast.LENGTH_SHORT
        ).show()
        updateUI(null)
    }

    private fun showSimpleProgressDialog() {
                val visibility = if (mProgressBar.visibility == View.GONE) View.VISIBLE else View.GONE
                mProgressBar.visibility = visibility
    }

    private fun removeSimpleProgressDialog() {
        if (mProgressBar.visibility==View.VISIBLE) {
            mProgressBar.visibility=View.GONE
        }
    }
    private fun updateUI(user: FirebaseUser?) {
        user?.also{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            return
        }
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

}