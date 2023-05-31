package com.makelick.taskmate2.model

data class Board(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val updatedAt: String,
    val createdAt: String,
    val members: List<Member>? = null,
    val issues: List<Issue>? = null
)