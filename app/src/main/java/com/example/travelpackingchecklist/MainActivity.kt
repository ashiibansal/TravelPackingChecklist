package com.example.travelpackingchecklist

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.travelpackingchecklist.model.Blog
import com.example.travelpackingchecklist.adapter.BlogAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private lateinit var addListBtn: Button
    private lateinit var adapter: ArrayAdapter<String>

    private val predefinedLists = listOf("Beach Trip", "Business Trip", "Camping")
    private val customLists = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        addListBtn = findViewById(R.id.addListBtn)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, predefinedLists + customLists)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val listName = adapter.getItem(position)
            val intent = Intent(this, ChecklistActivity::class.java)
            intent.putExtra("LIST_NAME", listName)
            startActivity(intent)
        }

        val blogList = listOf(
            Blog(
                "10 Essential Packing Tips",
                "Smart ways to save space and travel light.",
                R.drawable.blog1,
                "https://timus.in/blogs/news/top-10-essential-packing-tips-for-stress-free-travel?srsltid=AfmBOopFjgwy6T25XnIHKSqTWdsHf89sDo1oS-UcXYPx6n2ZQAVvnz88"
            ),
            Blog(
                "Beach Essentials You Shouldn’t Forget",
                "Everything you need for a perfect beach trip.",
                R.drawable.blog2,
                "https://www.buzzfeed.com/hbraga/beach-vacay-products"
            ),
            Blog(
                "How to Pack for a Business Trip",
                "Look sharp and travel efficiently with these tricks.",
                R.drawable.blog3,
                "https://safaribags.com/blogs/news/how-to-pack-for-business-travel-smart-tips-and-tricks"
            )
        )

        val blogListView = findViewById<ListView>(R.id.blogListView)
        val blogAdapter = BlogAdapter(this, blogList)
        blogListView.adapter = blogAdapter

        addListBtn.setOnClickListener {
            val input = EditText(this)
            val dialog = android.app.AlertDialog.Builder(this)
                .setTitle("New List")
                .setMessage("Enter list name:")
                .setView(input)
                .setPositiveButton("Add") { _, _ ->
                    val name = input.text.toString()
                    if (name.isNotEmpty()) {
                        customLists.add(name)
                        adapter.clear()
                        adapter.addAll(predefinedLists + customLists)
                        adapter.notifyDataSetChanged()
                    }
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }
    }
}