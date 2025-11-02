package com.example.travelpackingchecklist

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class AddItemActivity : AppCompatActivity() {

    private lateinit var itemNameInput: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var addBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        itemNameInput = findViewById(R.id.itemNameInput)
        categorySpinner = findViewById(R.id.categorySpinner)
        addBtn = findViewById(R.id.addBtn)

        val categories = listOf("Clothes", "Toiletries", "Electronics", "Documents", "Important", "Other")

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        categorySpinner.adapter = spinnerAdapter

        addBtn.setOnClickListener {
            val itemName = itemNameInput.text.toString().trim()
            val category = categorySpinner.selectedItem.toString()

            if (itemName.isEmpty()) {
                Toast.makeText(this, "Please enter an item name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = Intent()
            resultIntent.putExtra("ITEM_NAME", itemName)
            resultIntent.putExtra("ITEM_CATEGORY", category)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}