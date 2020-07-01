package com.example.myphotostock.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myphotostock.R
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var registerButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        registerButton = findViewById(R.id.register_button)
        emailEditText = findViewById(R.id.email_register_editText)
        passwordEditText = findViewById(R.id.password_register_editText)

        registerButton.setOnClickListener {
            registerUser()
        }


    }
    private fun registerUser() {
        if (emailEditText.text.toString().isEmpty()) {
            emailEditText.error = "Please enter a email"
            emailEditText.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()) {
            emailEditText.error = "Please enter valid email"
            emailEditText.requestFocus()
            return
        }
        if (passwordEditText.text.toString().isEmpty()) {
            passwordEditText.error = "Please enter a password"
            passwordEditText.requestFocus()
            return
        }
        if (passwordEditText.text.length <= 5) {
            passwordEditText.error = "Password must has minimum 6 characters"
            passwordEditText.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(baseContext, "Registration successful", Toast.LENGTH_SHORT).show()
                                auth.signOut()
                                startActivity(Intent(this, LoginActivity::class.java))
                                finish()
                            }
                        }

                } else {

                    Toast.makeText(baseContext, "Sign Up failed. Try again.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}