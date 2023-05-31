package com.makelick.taskmate2.model

data class Member(
    val id: Int,
    val updatedAt: String,
    val createdAt: String,
    val role: Roles,
    val user: User
)