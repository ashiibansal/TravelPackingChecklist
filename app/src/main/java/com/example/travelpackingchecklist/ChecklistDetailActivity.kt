package com.example.travelpackingchecklist

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.travelpackingchecklist.adapter.ChecklistAdapter
import com.example.travelpackingchecklist.model.ChecklistItem

class ChecklistDetailActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: ChecklistAdapter
    private val items = mutableListOf<ChecklistItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist_detail)

        val listName = intent.getStringExtra("listName")
        supportActionBar?.title = listName ?: "Checklist"

        listView = findViewById(R.id.itemListView)

        addCategory("Clothes", listOf("T-Shirts", "Jeans", "Jacket", "Socks"))
        addCategory("Accessories", listOf("Sunglasses", "Watch", "Belt"))
        addCategory("Toiletries", listOf("Toothbrush", "Shampoo", "Towel"))
        addCategory("Misc", listOf("Charger", "Water Bottle", "Snacks"))
        addCategory("Important", listOf("Passport", "Travel Tickets", "Wallet"))

        adapter = ChecklistAdapter(this, items)
        listView.adapter = adapter
    }

    // Helper function to add category headers and items
    private fun addCategory(category: String, itemNames: List<String>) {
        items.add(ChecklistItem(category, isHeader = true))
        for (name in itemNames) {
            items.add(ChecklistItem(name))
        }
    }
}