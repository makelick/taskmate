package com.makelick.taskmate2.model

data class IssueCreation(
    val title: String,
    val description: String,
    val status: Status,
)