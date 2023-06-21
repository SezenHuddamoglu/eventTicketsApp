package com.sezen.eventticketsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sezen.eventticketsapp.citySelection.CitySelection
import com.sezen.eventticketsapp.databinding.ActivityLoginBinding

class Login : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()

        //When the user clicks on the signupText, it redirects the user to the sign up page
        binding.signupText.setOnClickListener {
            val signupIntent = Intent(this, Signup::class.java)
            startActivity(signupIntent)
        }

        //This part shows what happening when user clicks to the login button
        binding.loginButton.setOnClickListener {
            val email = binding.emailET.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                // If email and password fields not empty
                // user can log in with email and password using FirebaseAuth
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        // If login is successful, user redirects to the citySelection page
                        val mainIntent = Intent(this, CitySelection::class.java)
                        startActivity(mainIntent)
                    } else {
                        // If login fails, it displays the error message in a Toast
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            } else {
                // If email or password is empty, it displays a toast asking to fill the empty fields
                Toast.makeText(this, "Please fill the empty fields!", Toast.LENGTH_SHORT).show()

            }
        }
    }

}