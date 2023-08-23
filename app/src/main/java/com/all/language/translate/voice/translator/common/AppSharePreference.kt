package com.all.language.translate.voice.translator.common

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class AppSharePreference @Inject constructor(private val context: Context) {
    companion object{
        const val APP_SHARE_KEY = "com.fatherofapps.androidbase"
        const val FIRST_LAUNCH = "com.fatherofapps.androidbase.first_launch"
        const val SELECTED_LANGUAGE = "selected_language"
    }

    fun getSharedPreferences(): SharedPreferences?{
        return context.getSharedPreferences(APP_SHARE_KEY,Context.MODE_PRIVATE)
    }

}