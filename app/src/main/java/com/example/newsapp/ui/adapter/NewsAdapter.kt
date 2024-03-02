package com.example.newsapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.newsapp.R
import com.example.newsapp.model.NewsItem

class NewsAdapter(
    private var newsList: List<NewsItem>,
    private val listener: OnItemClickListener
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

    interface OnItemClickListener {
        fun onItemClick(newsItem: NewsItem)
    }

    inner class NewsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        private val newsImageView: ImageView = itemView.findViewById(R.id.newsImageView)
        private val newsTitleTextView: TextView = itemView.findViewById(R.id.newsTitleTextView)
        private val newsDescriptionTextView: TextView = itemView.findViewById(R.id.newsDescriptionTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(newsList[position])
            }
        }

        fun bind(newsItem: NewsItem) {
            newsImageView.load(newsItem.urlToImage) {
                scale(Scale.FIT)
            }
            newsTitleTextView.text = newsItem.title
            newsDescriptionTextView.text = newsItem.description
            authorTextView.text = newsItem.author
            timeTextView.text = newsItem.publishedAt
        }
    }
}