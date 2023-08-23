package com.all.language.translate.voice.translator.data.models

import android.content.Context
import com.all.language.translate.voice.translator.R
import com.example.ap_translator.models.LanguageTrans
import com.google.mlkit.nl.translate.Translator

class SingletonLanguageTrans private constructor() {
    init {
        // define in constructor
// Create an English-German translator:
    }

    var languageType = ""
    var translator: Translator? = null

    var langTransFrom: LanguageTrans =
        LanguageTrans(name = "English", code = "en", avatar = R.drawable.color_united_kingdom)
    var langTransFromPositionSelected = 4
    var langTransTo: LanguageTrans =
        LanguageTrans(name = "French", code = "fr", avatar = R.drawable.color_france)
    var langTransToPositionSelected = 5

    private object Holder {
        val INSTANCE = SingletonLanguageTrans()
    }

    fun swapLang() {
        val languageTrans = langTransFrom
        val langPosition = langTransFromPositionSelected
        langTransFrom = langTransTo
        langTransFromPositionSelected = langTransToPositionSelected
        langTransTo = languageTrans
        langTransToPositionSelected = langPosition
    }

    companion object {
        @JvmStatic
        fun getInstance(): SingletonLanguageTrans {
            return Holder.INSTANCE
        }
    }
}

class DbConstants {
    companion object {
        val APP_NAME = "Voice Translator"

        fun getAppLable(context: Context): String {
            return context.getString(R.string.app_name)
        }

    }
}