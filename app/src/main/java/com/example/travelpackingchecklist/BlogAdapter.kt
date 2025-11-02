package com.example.travelpackingchecklist.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.BaseAdapter
import com.example.travelpackingchecklist.R
import com.example.travelpackingchecklist.model.Blog

class BlogAdapter(private val context: Context, private val blogs: List<Blog>) : BaseAdapter() {
    override fun getCount(): Int = blogs.size
    override fun getItem(position: Int): Any = blogs[position]
    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_blog, parent, false)

        val blog = blogs[position]

        val titleView = view.findViewById<TextView>(R.id.blogTitle)
        val descView = view.findViewById<TextView>(R.id.blogDescription)
        val imageView = view.findViewById<ImageView>(R.id.blogImage)
        val readMoreBtn = view.findViewById<Button>(R.id.readMoreBtn)

        titleView.text = blog.title
        descView.text = blog.description
        imageView.setImageResource(blog.imageResId)

        readMoreBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(blog.url))
            context.startActivity(intent)
        }

        return view
    }
}