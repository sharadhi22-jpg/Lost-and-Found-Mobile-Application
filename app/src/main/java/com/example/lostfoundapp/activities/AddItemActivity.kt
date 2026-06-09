package com.example.lostfoundapp.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.lostfoundapp.R
import com.example.lostfoundapp.database.DBHelper
import com.example.lostfoundapp.model.Item
import java.util.*

class AddItemActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var imgPreview: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val db = DBHelper(this)

        val etName = findViewById<EditText>(R.id.etName)
        val etDesc = findViewById<EditText>(R.id.etDesc)
        val etLocation = findViewById<EditText>(R.id.etLocation)
        val etDate = findViewById<EditText>(R.id.etDate)
        val etTime = findViewById<EditText>(R.id.etTime)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val radioLost = findViewById<RadioButton>(R.id.radioLost)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnImage = findViewById<Button>(R.id.btnImage)

        imgPreview = findViewById(R.id.imgPreview)

        // 📅 DATE PICKER
        etDate.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                etDate.setText("$d/${m + 1}/$y")
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        // ⏰ TIME PICKER
        etTime.setOnClickListener {
            val c = Calendar.getInstance()
            TimePickerDialog(this, { _, h, min ->
                etTime.setText(String.format("%02d:%02d", h, min))
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
        }

        // 🖼 IMAGE PICKER (simple + stable)
        btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "image/*"
                addCategory(Intent.CATEGORY_OPENABLE)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivityForResult(intent, 100)
        }

        // 💾 SAVE ITEM
        btnSave.setOnClickListener {

            val name = etName.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val location = etLocation.text.toString().trim()
            val date = etDate.text.toString()
            val time = etTime.text.toString()
            val phone = etPhone.text.toString().trim()

            // ✅ VALIDATION
            if (name.isEmpty() || desc.isEmpty() || location.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val item = Item(
                id = 0,
                name = name,
                description = desc,
                type = if (radioLost.isChecked) "Lost" else "Found",
                date = "$date $time",
                location = location,
                phone = phone,
                imageUri = selectedImageUri?.toString() ?: "",
                responses = "",
                status = "active"
            )

            db.insertItem(item)

            Toast.makeText(this, "Item saved successfully", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    // 🖼 IMAGE RESULT
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            val uri = data?.data

            if (uri != null) {
                try {
                    // ✅ SIMPLE & CORRECT (no flag error)
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )

                    selectedImageUri = uri
                    imgPreview.setImageURI(uri)

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}