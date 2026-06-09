package com.example.lostfoundapp.activities

import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.lostfoundapp.R
import com.example.lostfoundapp.database.DBHelper

class ItemDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val tvName = findViewById<TextView>(R.id.tvName)
        val tvDesc = findViewById<TextView>(R.id.tvDesc)
        val tvPhone = findViewById<TextView>(R.id.tvPhone)
        val tvChat = findViewById<TextView>(R.id.tvChat)
        val etMsg = findViewById<EditText>(R.id.etMsg)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val btnReceived = findViewById<Button>(R.id.btnReceived)
        val imgItem = findViewById<ImageView>(R.id.imgItem)

        val db = DBHelper(this)

        val sharedPref = getSharedPreferences("User", MODE_PRIVATE)
        val userName = sharedPref.getString("name", "User")

        val id = intent.getIntExtra("id", -1)

        val item = db.getItemById(id)

        if (item == null) {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // ✅ SET DATA
        tvName.text = item.name
        tvDesc.text = item.description
        tvPhone.text = "📞 ${item.phone}"

        tvChat.text = if (item.responses.isNotEmpty()) {
            item.responses
        } else {
            "No messages yet"
        }

        // ✅ SAFE IMAGE LOAD (ONLY ONCE)
        try {
            if (item.imageUri.isNotEmpty()) {
                imgItem.setImageURI(Uri.parse(item.imageUri))
            } else {
                imgItem.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        } catch (e: Exception) {
            imgItem.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // ✅ SEND MESSAGE
        btnSend.setOnClickListener {
            val msg = etMsg.text.toString().trim()

            if (msg.isNotEmpty()) {

                val fullMessage = "$userName: $msg"
                db.addResponse(id, fullMessage)

                val updatedItem = db.getItemById(id)
                tvChat.text = updatedItem?.responses ?: "No messages yet"

                etMsg.text.clear()

                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Enter message", Toast.LENGTH_SHORT).show()
            }
        }

        // ✅ RECEIVED BUTTON
        btnReceived.setOnClickListener {
            if (id != -1) {
                db.markReceived(id)
                Toast.makeText(this, "Marked as received", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Invalid item", Toast.LENGTH_SHORT).show()
            }
        }
    }
}