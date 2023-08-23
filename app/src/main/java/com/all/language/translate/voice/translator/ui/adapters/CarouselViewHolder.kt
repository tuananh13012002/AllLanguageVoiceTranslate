package com.all.language.translate.voice.translator.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.all.language.translate.voice.translator.databinding.CarouselItemBinding

class CarouselViewHolder(
    private val binding: CarouselItemBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(entity: Triple<Int, String, String>) {
        binding.imageViewCarouselItem.setImageResource(entity.first)
        binding.titleImageIntro.text = entity.second
        binding.tvContent.text = entity.third
    }

    companion object {
        fun create(
            viewGroup: ViewGroup,
        ): CarouselViewHolder {
            return CarouselViewHolder(
                binding = CarouselItemBinding.inflate(
                    LayoutInflater.from(viewGroup.context),
                    viewGroup,
                    false
                ),
            )
        }
    }
}