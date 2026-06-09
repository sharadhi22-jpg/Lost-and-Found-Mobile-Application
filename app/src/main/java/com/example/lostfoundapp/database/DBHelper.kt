package com.example.lostfoundapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.lostfoundapp.model.Item

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "LostFoundDB", null, 4) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE items (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "description TEXT, " +
                    "type TEXT, " +
                    "date TEXT, " +
                    "location TEXT, " +
                    "phone TEXT, " +
                    "imageUri TEXT, " +
                    "responses TEXT, " +
                    "status TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS items")
        onCreate(db)
    }

    // ✅ INSERT ITEM
    fun insertItem(item: Item) {
        val db = writableDatabase
        val values = ContentValues()

        values.put("name", item.name)
        values.put("description", item.description)
        values.put("type", item.type)
        values.put("date", item.date)
        values.put("location", item.location)
        values.put("phone", item.phone)
        values.put("imageUri", item.imageUri)
        values.put("responses", "")
        values.put("status", "active")

        db.insert("items", null, values)
        db.close()
    }

    // ✅ GET ONLY ACTIVE ITEMS
    fun getAllItems(): List<Item> {
        val list = mutableListOf<Item>()
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM items WHERE status='active'",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Item(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8) ?: "",
                        cursor.getString(9) ?: "active"
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return list
    }

    // ✅ GET ITEM BY ID (VERY IMPORTANT FOR CHAT PERSISTENCE)
    fun getItemById(id: Int): Item? {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM items WHERE id=?",
            arrayOf(id.toString())
        )

        var item: Item? = null

        if (cursor.moveToFirst()) {
            item = Item(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8) ?: "",
                cursor.getString(9) ?: "active"
            )
        }

        cursor.close()
        db.close()
        return item
    }

    // ✅ ADD RESPONSE (APPEND CHAT)
    fun addResponse(id: Int, newMsg: String) {
        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT responses FROM items WHERE id=?",
            arrayOf(id.toString())
        )

        var old = ""
        if (cursor.moveToFirst()) {
            old = cursor.getString(0) ?: ""
        }
        cursor.close()

        val updated = if (old.isEmpty()) newMsg else "$old\n$newMsg"

        val values = ContentValues()
        values.put("responses", updated)

        val writeDb = writableDatabase
        writeDb.update("items", values, "id=?", arrayOf(id.toString()))
        writeDb.close()
    }

    // ✅ MARK AS RECEIVED (SOFT DELETE)
    fun markReceived(id: Int) {
        val db = writableDatabase

        val values = ContentValues()
        values.put("status", "received")

        db.update("items", values, "id=?", arrayOf(id.toString()))
        db.close()
    }

    // ✅ OPTIONAL HARD DELETE
    fun deleteItem(id: Int) {
        val db = writableDatabase
        db.delete("items", "id=?", arrayOf(id.toString()))
        db.close()
    }
}