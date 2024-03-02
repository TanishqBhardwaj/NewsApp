package com.example.newsapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.model.NewsItem

class NewsAdapter(
    private var newsList: List<NewsItem>,
): RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem = newsList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val newsImageView: ImageView = itemView.findViewById(R.id.newsImageView)
        private val newsTextView: TextView = itemView.findViewById(R.id.newsTextView)

        fun bind(newsItem: NewsItem) {
            newsTextView.text = newsItem.title
        }
    }
}