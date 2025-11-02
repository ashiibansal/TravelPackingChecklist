package com.example.travelpackingchecklist.model

data class ChecklistItem(
    val name: String,
    val category: String = "",
    var isChecked: Boolean = false,
    val isHeader: Boolean = false
)