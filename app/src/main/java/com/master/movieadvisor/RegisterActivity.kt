package com.master.movieadvisor

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.master.movieadvisor.databinding.ActivityRegisterBinding
import com.master.movieadvisor.model.User
import com.master.movieadvisor.service.dto.SignInDTO
import com.master.movieadvisor.service.providers.NetworkListener
import com.master.movieadvisor.service.providers.NetworkProvider


class RegisterActivity : AppCompatActivity()  {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private val TAG = "RegisterActivity"
    private val database = Firebase.database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.sex_profile,
            R.layout.custom_spinner
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            binding.spinnerSex.adapter = adapter
        }
        auth = Firebase.auth
        with(binding){
            register.setOnClickListener {
                if(registerPassword.text.isNotBlank() && registerPassword.text.toString() == passwordConfirm.text.toString()){
                    toRegister()
                }else{
                    Log.d("AZE","ECHEC")
                }
            }
        }

    }

    private fun toRegister() {
        auth.createUserWithEmailAndPassword(binding.registerEmail.text.toString(), binding.registerPassword.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Votre compte a été créé", Toast.LENGTH_LONG)
                        .show()
                    val user = auth.currentUser
                    val userDb = User(name = binding.registerName.text.toString(),
                        firstName = binding.registerFirstName.text.toString(),
                        sex = binding.spinnerSex.selectedItem.toString()

                        )

                    user?.let {
                        val database = Firebase.database.reference
                        database.child("users").child(it.uid).setValue(userDb)

                    }
                    NetworkProvider.signIn(SignInDTO(userIdAndroid = user!!.uid,username = "${userDb.firstName} ${userDb.name}"),object : NetworkListener<String>{
                        override fun onSuccess(data: String) {
                            Log.d("OKI", "Okkkkkkk")
                        }

                        override fun onError(throwable: Throwable) {
                            Log.e("Error", throwable.localizedMessage)
                        }

                    })
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)

                }

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
    fun writeUserData(user: User) {
        database.getReference("users/" + auth.uid!!).setValue({
            user
            //some more user data
        })
    }
}