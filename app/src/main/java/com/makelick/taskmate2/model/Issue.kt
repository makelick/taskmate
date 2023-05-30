package com.makelick.taskmate2.model

data class Issue(
    val title: String,
    val description: String,
    val status: Status,
)