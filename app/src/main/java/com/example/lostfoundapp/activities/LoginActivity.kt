package com.example.lostfoundapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.lostfoundapp.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("User", MODE_PRIVATE)

        // ✅ AUTO LOGIN
        val remember = prefs.getBoolean("remember", false)
        if (remember) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val cbRemember = findViewById<CheckBox>(R.id.cbRemember)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        val savedEmail = prefs.getString("email", null)
        val savedPassword = prefs.getString("password", null)

        btnLogin.setOnClickListener {

            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            // ✅ validation
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!email.endsWith("@bmsce.ac.in")) {
                Toast.makeText(this, "Use BMSCE email only", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ❗ CHECK IF USER EXISTS
            if (savedEmail == null || savedPassword == null) {
                Toast.makeText(this, "Please register first", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RegisterActivity::class.java))
                return@setOnClickListener
            }

            // ✅ LOGIN CHECK
            if (email == savedEmail && password == savedPassword) {

                prefs.edit()
                    .putBoolean("remember", cbRemember.isChecked)
                    .apply()

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, DashboardActivity::class.java))
                finish()

            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        // 🔁 GO TO REGISTER
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}