package com.example.travelpackingchecklist

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.travelpackingchecklist.adapter.ChecklistAdapter
import com.example.travelpackingchecklist.model.ChecklistItem
import org.json.JSONArray
import org.json.JSONObject

class ChecklistActivity : AppCompatActivity() {

    private lateinit var titleView: TextView
    private lateinit var listView: ListView
    private lateinit var addItemBtn: Button
    private lateinit var backButton: Button
    private lateinit var deleteListButton: Button
    private lateinit var adapter: ChecklistAdapter
    private lateinit var sharedPreferences: SharedPreferences

    private val items = mutableListOf<ChecklistItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist)

        titleView = findViewById(R.id.listTitle)
        listView = findViewById(R.id.itemListView)
        addItemBtn = findViewById(R.id.addItemBtn)
        backButton = findViewById(R.id.backButton)
        deleteListButton = findViewById(R.id.deleteListButton)

        sharedPreferences = getSharedPreferences("ChecklistPrefs", MODE_PRIVATE)

        val listName = intent.getStringExtra("LIST_NAME") ?: "Checklist"
        titleView.text = listName

        loadChecklist(listName)

        adapter = ChecklistAdapter(this, items)
        listView.adapter = adapter

        addItemBtn.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivityForResult(intent, 1)
        }

        backButton.setOnClickListener { finish() }

        deleteListButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete List")
            builder.setMessage("Are you sure you want to delete this list?")
            builder.setPositiveButton("Yes") { _, _ ->
                sharedPreferences.edit().remove(listName).apply()
                Toast.makeText(this, "List deleted", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            builder.setNegativeButton("Cancel", null)
            builder.show()
        }
    }

    private fun addCategory(category: String, itemNames: List<String>) {
        // Add category header first
        items.add(ChecklistItem(category, isHeader = true))

        for (itemName in itemNames) {
            items.add(ChecklistItem(itemName, category))
        }
    }

    private fun loadChecklist(listName: String) {
        items.clear()
        val savedData = sharedPreferences.getString(listName, null)

        if (savedData != null) {
            val jsonArray = JSONArray(savedData)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val name = obj.getString("name")
                val category = obj.optString("category", "")
                val isHeader = obj.optBoolean("isHeader", false)
                items.add(ChecklistItem(name, category, isChecked = false, isHeader = isHeader))
            }
        } else {
            when (listName) {
                "Beach Trip" -> {
                    addCategory("Clothes", listOf("Swimsuit", "Beach Towel", "Flip Flops"))
                    addCategory("Accessories", listOf("Sunglasses", "Hat", "Beach Bag"))
                    addCategory("Toiletries", listOf("Sunscreen", "Body Lotion", "Comb"))
                    addCategory("Misc", listOf("Book", "Camera"))
                    addCategory("Important", listOf("ID Card", "Cash", "Travel Tickets"))
                }
                "Business Trip" -> {
                    addCategory("Clothes", listOf("Formal Shirt", "Blazer", "Trousers"))
                    addCategory("Accessories", listOf("Watch", "Tie", "Laptop Bag"))
                    addCategory("Toiletries", listOf("Toothbrush", "Deodorant", "Shaving Kit"))
                    addCategory("Misc", listOf("Charger", "Notebook", "Pens"))
                    addCategory("Important", listOf("Laptop", "Wallet", "Office ID"))
                }
                "Camping" -> {
                    addCategory("Clothes", listOf("Jacket", "Trekking Shoes", "Socks"))
                    addCategory("Accessories", listOf("Cap", "Flashlight", "Multi-tool"))
                    addCategory("Toiletries", listOf("Soap", "Toothpaste", "Towel"))
                    addCategory("Misc", listOf("Tent", "Sleeping Bag", "Snacks"))
                    addCategory("Important", listOf("First Aid Kit", "Map", "Phone"))
                }
                else -> {
                    addCategory("Clothes", listOf("T-Shirts", "Jeans"))
                    addCategory("Accessories", listOf("Watch"))
                    addCategory("Toiletries", listOf("Toothbrush"))
                    addCategory("Misc", listOf("Charger"))
                    addCategory("Important", listOf("Passport"))
                }
            }
        }
    }

    private fun saveChecklist(listName: String) {
        val jsonArray = JSONArray()
        for (item in items) {
            val obj = JSONObject()
            obj.put("name", item.name)
            obj.put("category", item.category)
            obj.put("isHeader", item.isHeader)
            jsonArray.put(obj)
        }
        sharedPreferences.edit().putString(listName, jsonArray.toString()).apply()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            val newItem = data?.getStringExtra("ITEM_NAME") ?: return
            val category = data.getStringExtra("ITEM_CATEGORY") ?: "Other"
            val listName = titleView.text.toString()

            val headerIndex = items.indexOfFirst { it.isHeader && it.name.equals(category, ignoreCase = true) }

            if (headerIndex != -1) {
                val nextHeaderIndex = items.indexOfFirst { it.isHeader && items.indexOf(it) > headerIndex }

                if (nextHeaderIndex != -1) {
                    items.add(nextHeaderIndex, ChecklistItem(newItem, category))
                } else {
                    items.add(ChecklistItem(newItem, category))
                }
            } else {
                items.add(ChecklistItem(category, isHeader = true))
                items.add(ChecklistItem(newItem, category))
            }

            adapter.notifyDataSetChanged()
            saveChecklist(listName)
        }
    }
}