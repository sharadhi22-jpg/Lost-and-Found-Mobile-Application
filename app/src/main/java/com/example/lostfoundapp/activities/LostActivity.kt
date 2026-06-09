package com.example.lostfoundapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfoundapp.R
import com.example.lostfoundapp.adapter.ItemAdapter
import com.example.lostfoundapp.database.DBHelper

class LostActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnAdd: Button
    private lateinit var tvEmpty: TextView
    private lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lost)

        recyclerView = findViewById(R.id.recyclerView)
        tvEmpty = findViewById(R.id.tvEmpty)

        db = DBHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this)

    }

    override fun onResume() {
        super.onResume()
        loadLostItems()
    }

    private fun loadLostItems() {

        val allItems = db.getAllItems()

        // 🔴 Filter LOST only
        val lostItems = allItems.filter { it.type.equals("Lost", true) }

        if (lostItems.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
        } else {
            tvEmpty.visibility = View.GONE
        }

        recyclerView.adapter = ItemAdapter(lostItems)
    }
}