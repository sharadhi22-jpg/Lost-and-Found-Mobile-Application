package com.example.lostfoundapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lostfoundapp.R

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val btnLost = findViewById<Button>(R.id.btnLost)
        val btnFound = findViewById<Button>(R.id.btnFound)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val tvWelcome = findViewById<TextView>(R.id.tvWelcome)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)

        val prefs = getSharedPreferences("User", MODE_PRIVATE)

        // ✅ SHOW CURRENT USER
        val name = prefs.getString("name", "User")
        val email = prefs.getString("email", "")

        tvWelcome.text = "Welcome, $name 👋"
        tvEmail.text = email

        // NAVIGATION
        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        btnLost.setOnClickListener {
            startActivity(Intent(this, LostActivity::class.java))
        }

        btnFound.setOnClickListener {
            startActivity(Intent(this, FoundActivity::class.java))
        }

        // ✅ LOGOUT (VERY IMPORTANT FIX)
        btnLogout.setOnClickListener {

            val editor = prefs.edit()
            editor.clear()   // 🔥 removes old user completely
            editor.apply()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}