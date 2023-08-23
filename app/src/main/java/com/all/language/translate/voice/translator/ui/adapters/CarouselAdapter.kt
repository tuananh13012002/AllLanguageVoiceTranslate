package com.all.language.translate.voice.translator.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.all.language.translate.voice.translator.R

class CarouselAdapter(private val items: List<Triple<Int, String, String>>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        return CarouselViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        items[position].let { (holder as CarouselViewHolder).bind(it) }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}