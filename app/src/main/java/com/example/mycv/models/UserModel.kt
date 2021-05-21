package com.example.mycv.models

data class UserModel(
    val _id: String,
    val userName: String,
    val gender: Int,
    val dateOfBirth: String,
    val cellphone: String,
    val email: String,
    val password: String,
    val address: String,
    val specialization: String,
    val description: String
)
