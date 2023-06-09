package com.makelick.taskmate2.model

data class Issue(
    val id: Int,
    val updatedAt: String,
    val createdAt: String,
    val title: String,
    val description: String,
    val status: Status,
)