package com.sezen.eventticketsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sezen.eventticketsapp.citySelection.CitySelection
import com.sezen.eventticketsapp.databinding.ActivitySignupBinding

class Signup : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This part inflates the layout using ViewBinding
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        //When the user clicks on the loginText, it redirects the user to the log in page
        binding.loginText.setOnClickListener {
            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
        }

        //This part shows what happening when user clicks to the sign up button
        binding.singupButton.setOnClickListener {
            val email = binding.emailET.text.toString()
            val pass = binding.passET.text.toString()
            val confirmPass = binding.confirmPassET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                //If all the fields are not empty, it checks password and comfirm passwords are the same
                if (pass == confirmPass) {
                    // If password and confirm password fields are the same
                    // user can sign up with email and password using FirebaseAuth
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            // If sign up is successful, user redirects to the citySelection page
                            val intent = Intent(this, CitySelection::class.java)
                            startActivity(intent)
                        } else {
                            // If sign up fails, it displays the error message in a Toast
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                // If email or password is empty, it displays a toast asking to fill the empty fields
                Toast.makeText(this, "Please fill the empty fields!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}