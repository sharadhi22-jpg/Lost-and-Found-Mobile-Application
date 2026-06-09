package com.example.lostfoundapp.utils

import android.content.Context
import android.widget.Toast

object CustomToast {
    fun show(context: Context, msg: String) {
        Toast.makeText(context, "✨ $msg", Toast.LENGTH_LONG).show()
    }
}