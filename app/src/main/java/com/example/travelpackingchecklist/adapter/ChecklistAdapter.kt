package com.example.travelpackingchecklist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import com.example.travelpackingchecklist.R
import com.example.travelpackingchecklist.model.ChecklistItem
import android.graphics.Typeface

class ChecklistAdapter(
    private val context: Context,
    private val items: MutableList<ChecklistItem>
) : BaseAdapter() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    override fun getCount(): Int = items.size
    override fun getItem(position: Int): Any = items[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getViewTypeCount(): Int = 2

    override fun getItemViewType(position: Int): Int {
        return if (items[position].isHeader) TYPE_HEADER else TYPE_ITEM
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = items[position]

        return if (item.isHeader) {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.item_header, parent, false)
            val headerText = view.findViewById<TextView>(R.id.headerText)
            headerText.text = item.name
            headerText.setTypeface(null, Typeface.BOLD)
            view.isEnabled = false
            view.isFocusable = false
            view
        } else {
            val view = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.item_checklist, parent, false)

            val checkBox = view.findViewById<CheckBox>(R.id.itemCheckBox)
            val deleteButton = view.findViewById<ImageButton>(R.id.deleteButton)

            checkBox.text = item.name
            checkBox.isChecked = item.isChecked
            checkBox.paint.isStrikeThruText = item.isChecked
            checkBox.alpha = if (item.isChecked) 0.5f else 1.0f

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                item.isChecked = isChecked
                checkBox.paint.isStrikeThruText = isChecked
                checkBox.alpha = if (isChecked) 0.5f else 1.0f
            }

            deleteButton.setOnClickListener {
                items.removeAt(position)
                notifyDataSetChanged()
            }

            view
        }
    }
}