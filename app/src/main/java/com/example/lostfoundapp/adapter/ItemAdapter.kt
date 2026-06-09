package com.example.lostfoundapp.adapter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lostfoundapp.R
import com.example.lostfoundapp.activities.ItemDetailActivity
import com.example.lostfoundapp.model.Item

class ItemAdapter(private val list: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val type: TextView = itemView.findViewById(R.id.tvType)
        val image: ImageView = itemView.findViewById(R.id.imgItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        holder.name.text = item.name
        holder.type.text = item.type

        // Reset image first (VERY IMPORTANT)
        holder.image.setImageResource(android.R.drawable.ic_menu_gallery)

        // Load image safely
        if (item.imageUri.isNotEmpty()) {
            try {
                val uri = Uri.parse(item.imageUri)
                holder.image.setImageURI(uri)
            } catch (e: Exception) {
                holder.image.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ItemDetailActivity::class.java)
            intent.putExtra("id", item.id)
            context.startActivity(intent)
        }

        // Load image
        if (item.imageUri.isNotEmpty()) {
            try {
                holder.image.setImageURI(Uri.parse(item.imageUri))
            } catch (e: Exception) {
                holder.image.setImageResource(android.R.drawable.ic_menu_gallery)
            }
        } else {
            holder.image.setImageResource(android.R.drawable.ic_menu_gallery)
        }

        // ✅ CLICK → OPEN DETAIL PAGE
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ItemDetailActivity::class.java)

            // 🔥 MOST IMPORTANT (FIX)
            intent.putExtra("id", item.id)

            // optional (safe to keep)
            intent.putExtra("name", item.name)
            intent.putExtra("desc", item.description)
            intent.putExtra("phone", item.phone)
            intent.putExtra("imageUri", item.imageUri)

            context.startActivity(intent)
        }
    }
}