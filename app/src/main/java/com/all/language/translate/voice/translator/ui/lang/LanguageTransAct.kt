package com.all.language.translate.voice.translator.ui.lang

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.all.language.translate.voice.translator.base.activities.BaseActivity
import com.all.language.translate.voice.translator.databinding.ActivityLanguageTransBinding
import com.all.language.translate.voice.translator.data.models.SingletonLanguageTrans

class LanguageTransAct : BaseActivity() {
    private lateinit var binding: ActivityLanguageTransBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageTransBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bindLanguageToTransRcv()
        binding.langToTransBtnBack.setOnClickListener {
            finish()
        }
    }


    private fun bindLanguageToTransRcv() {
        val rcvReading: RecyclerView?
        val typeLang = intent.getStringExtra(TYPE_LANGUAGE)

        rcvReading = binding.langToTransRcvLang
        rcvReading.adapter = ItemLanguageTransAdapter(
            ItemLanguageTransAdapter.dummyData,
            this,
            if (typeLang == "from") {
                SingletonLanguageTrans.getInstance().langTransFromPositionSelected
            } else {
                SingletonLanguageTrans.getInstance().langTransToPositionSelected
            },
        ) { position, item ->
            //call back
            if (typeLang == "from") {
                SingletonLanguageTrans.getInstance().langTransFrom = item
                SingletonLanguageTrans.getInstance().langTransFromPositionSelected = position
            } else {
                SingletonLanguageTrans.getInstance().langTransTo = item
                SingletonLanguageTrans.getInstance().langTransToPositionSelected = position
            }

        }
    }

    companion object {
         const val TYPE_LANGUAGE = "LanguageTransAct-Type"
        fun getIntent(context: Context, type: String): Intent {
            val intent = Intent(context, LanguageTransAct::class.java)
            intent.putExtra(TYPE_LANGUAGE, type)
            return intent
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}