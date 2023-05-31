package com.makelick.taskmate2.model

data class User(
    val id: String,
    val email: String,
    val profileImageUrl: String,
    val firstName: String,
    val lastName: String
)