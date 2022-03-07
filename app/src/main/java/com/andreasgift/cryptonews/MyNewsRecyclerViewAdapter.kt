package com.andreasgift.cryptonews

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.andreasgift.cryptonews.databinding.ItemNewsBinding
import com.andreasgift.cryptonews.model.News


class MyNewsRecyclerViewAdapter(
) : RecyclerView.Adapter<MyNewsRecyclerViewAdapter.ViewHolder>() {

    val dataList = arrayListOf<News>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.title.text = item.title
        holder.source.text = item.source
    }

    override fun getItemCount(): Int = dataList.size

    fun setData(list: List<News>){
        dataList.clear()
        dataList.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.title
        val source: TextView = binding.source
    }

}