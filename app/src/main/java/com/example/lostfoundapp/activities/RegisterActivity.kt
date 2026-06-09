package com.example.lostfoundapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.lostfoundapp.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etName = findViewById<EditText>(R.id.etName)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        val prefs = getSharedPreferences("User", MODE_PRIVATE)

        btnRegister.setOnClickListener {

            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.endsWith("@bmsce.ac.in")) {
                Toast.makeText(this, "Use BMSCE email only", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 4) {
                Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ SAVE USER
            prefs.edit()
                .putString("name", name)
                .putString("email", email)
                .putString("password", password)
                .apply()

            Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // 🔁 Go to Login
        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}