package com.master.movieadvisor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.master.movieadvisor.ui.toEditable

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var editTextEmail: EditText
    private lateinit var btnForReset: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth = Firebase.auth
        btnForReset = findViewById(R.id.btn_for_reset_password)
        editTextEmail = findViewById(R.id.email_for_reset_password)
        val bundle = intent.extras
        val emailUsed = bundle?.getString("email")
        emailUsed?.let {
            editTextEmail.text = emailUsed.toEditable()
        }
        btnForReset.setOnClickListener {
            val userEmail = editTextEmail.text

            if(userEmail.isNotBlank()){

                auth.sendPasswordResetEmail(userEmail.toString()).addOnCompleteListener(this) {  task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Vous avez reçu un mail", Toast.LENGTH_SHORT).show()
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(
                            baseContext, "Echec: envoi de mail.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


    }
}