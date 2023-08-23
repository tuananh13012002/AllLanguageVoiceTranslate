package com.all.language.translate.voice.translator.adapters

//noinspection SuspiciousImport
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.all.language.translate.voice.translator.R
import com.example.speaktotext.models.SpinnerItem

class CustomSpinnerAdapter(private val context: Context, private val items: List<SpinnerItem>) :
    ArrayAdapter<SpinnerItem>(context, R.layout.custom_spinner_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createCustomView(position, convertView, parent)
    }

    private fun createCustomView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.custom_spinner_item,
            parent,
            false
        )

        val item = getItem(position)
        view.findViewById<TextView>(R.id.nameCountry)?.text = item?.name
        val flag = context.resources.getIdentifier(item?.flag, "drawable", context.packageName)
        view.findViewById<ImageView>(R.id.imageView)?.setImageResource(flag)

        return view
    }
}