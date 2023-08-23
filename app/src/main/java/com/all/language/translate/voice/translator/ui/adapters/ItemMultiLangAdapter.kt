package com.all.language.translate.voice.translator.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.all.language.translate.voice.translator.R
import com.all.language.translate.voice.translator.databinding.ItemMultiLangBinding
import com.all.language.translate.voice.translator.ui.utils.SystemUtil
import com.example.ap_translator.models.LanguageTrans

class ItemMultiLangAdapter(
    private var list: List<LanguageTrans>,
    private val context: Context,
    private var selectedPosition: Int,
    val callBack: (Int, LanguageTrans) -> Unit
) : RecyclerView.Adapter<ItemMultiLangAdapter.ViewHolder>() {

    private var selectCode: String? = null
    private var selectedLanguageCode: String? = null

    companion object {
        val dummyData = listOf(
            LanguageTrans(name = "English", code = "en", avatar = R.drawable.color_united_kingdom),
            LanguageTrans(name = "French", code = "fr", avatar = R.drawable.color_france),
            LanguageTrans(name = "German", code = "de", avatar = R.drawable.color_germany),
            LanguageTrans(name = "Hindi", code = "hi", avatar = R.drawable.color_india),
            LanguageTrans(name = "Spanish", code = "es", avatar = R.drawable.color_spain),
            LanguageTrans(name = "Vietnamese", code = "vi", avatar = R.drawable.color_vietnam),
        )
    }


    class ViewHolder(val binding: ItemMultiLangBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("UseCompatLoadingForColorStateLists")
        fun bind(item: LanguageTrans, isSelected: Boolean) {
            binding.apply {
                if (isSelected) {
                    SystemUtil.setPreLanguage(context, item.code)
                    SystemUtil.setLocale(context)
                    itemLangImgCheck.setImageResource(R.drawable.checked)
                } else {
                    itemLangImgCheck.setImageResource(R.drawable.not_checked)
                }
                itemLangToTransTvName.text = item.name
                item.avatar?.let { itemLangToTransImgAva.setImageResource(it) }
            }
        }


        companion object {
            fun from(parent: ViewGroup, context: Context): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMultiLangBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, context)
            }
        }
    }

    fun update(data: List<LanguageTrans>?) {
        list = data ?: listOf()
        notifyDataSetChanged()
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(viewGroup, context)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item = list[position]
        viewHolder.bind(item, position == selectedPosition)
        viewHolder.binding.root.setOnClickListener {
            list[selectedPosition].isSelected = false
            selectedPosition = position
            list[selectedPosition].isSelected = true
            notifyDataSetChanged()
            callBack(position, item)
            savePositionLang(position)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = list.count()

    private fun savePositionLang(position: Int) {
        val pref = context.getSharedPreferences(
            "myPref", AppCompatActivity.MODE_PRIVATE
        )
        val editor = pref.edit()
        editor.putInt("positionLang", position)
        editor.commit()
    }

}