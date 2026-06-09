package com.example.lostfoundapp.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfoundapp.R
import com.example.lostfoundapp.adapter.ItemAdapter
import com.example.lostfoundapp.database.DBHelper

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var btnAdd: Button
    lateinit var db: DBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        btnAdd = findViewById(R.id.btnAdd)

        db = DBHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // ➕ Add Item
        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val items = db.getAllItems()
        recyclerView.adapter = ItemAdapter(items)
    }
}