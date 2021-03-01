package com.WillyFisky.mobile911

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //check bundle
        val bundle: Bundle? = intent.extras

        if (bundle != null){
            println("Bundle: ${bundle.getStringArray("data")}")
        }

        //Take inputs
        val email = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val loginSpinner = findViewById<ProgressBar>(R.id.loginSpinner)

        mAuth = FirebaseAuth.getInstance()

        loginBtn.setOnClickListener {
            loginSpinner.isVisible = true
            checkAuthentication(email, password, loginSpinner)
        }
    }

    private fun checkAuthentication(email: EditText, password: EditText, loginSpinner: ProgressBar){
        val user = email.text.toString()
        val pass = password.text.toString()
        if (user != "" && pass != ""){
            mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    println("Success")
                    loginSpinner.isVisible = false
                    val intent = Intent(this@Login, AdminActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    loginSpinner.isVisible = false
                    Toast.makeText(this@Login, "Authentication failed", Toast.LENGTH_SHORT).show()
                    println("Not Success")
                }
            }
        }else{
            Toast.makeText(this@Login, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}