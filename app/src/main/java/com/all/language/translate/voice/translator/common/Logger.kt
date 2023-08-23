package com.all.language.translate.voice.translator.common

import android.util.Log
import com.all.language.translate.voice.translator.BuildConfig

object Logger {

    fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message)
        }
    }

}