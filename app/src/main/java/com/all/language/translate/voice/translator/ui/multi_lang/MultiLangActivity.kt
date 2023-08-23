package com.all.language.translate.voice.translator.ui.multi_lang

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.all.language.translate.voice.translator.R
import com.all.language.translate.voice.translator.activities.MainActivity
import com.all.language.translate.voice.translator.base.activities.BaseActivity
import com.all.language.translate.voice.translator.databinding.ActivityMultiLangBinding
import com.all.language.translate.voice.translator.ui.adapters.ItemMultiLangAdapter
import com.all.language.translate.voice.translator.ui.intro.IntroAct
import com.all.language.translate.voice.translator.ui.utils.SystemUtil
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MultiLangActivity : BaseActivity() {

    var type: Int? = null
    private lateinit var binding: ActivityMultiLangBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this@MultiLangActivity, R.layout.activity_multi_lang)
        type = intent.getIntExtra(TYPE_LANG, 0)
        bindRv()
        checkType()
    }

    private fun checkType() {
        when (type) {
            1 -> {
                binding.typeLang1.visibility = View.VISIBLE
                binding.typeLang2.visibility = View.GONE
                binding.btnChooseLang.setOnClickListener {
                    startActivity(Intent(this, IntroAct::class.java))
                    finish()
                }
            }

            2 -> {
                binding.typeLang1.visibility = View.GONE
                binding.typeLang2.visibility = View.VISIBLE
                binding.imgBack.setOnClickListener {
                    onBackPressed()
                }
            }
        }
    }

    private fun bindRv() {
        findViewById<RecyclerView>(R.id.rcv_langs).adapter = ItemMultiLangAdapter(
            ItemMultiLangAdapter.dummyData,
            this,
            getPosition(),
        ) { _, item ->
            SystemUtil.setPreLanguage(this, item.code)
            SystemUtil.setLocale(this)

        }
    }

    companion object {
        private const val TYPE_LANG = "MultiLangAct_Lang"
        fun getIntent(context: Context, type: Int): Intent {
            val intent = Intent(context, MultiLangActivity::class.java)
            intent.putExtra(TYPE_LANG, type)
            return intent
        }
    }

    private fun getPosition(): Int {
        val pref = applicationContext.getSharedPreferences("myPref", MODE_PRIVATE)
        return pref.getInt("positionLang", 0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (type == 2) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            finish()
        }
    }
}