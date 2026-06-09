package com.example.lostfoundapp.model

data class Item(
    val id: Int,
    val name: String,
    val description: String,
    val type: String,
    val date: String,
    val location: String,
    val phone: String,
    val imageUri: String,
    val responses: String,
    val status: String
)