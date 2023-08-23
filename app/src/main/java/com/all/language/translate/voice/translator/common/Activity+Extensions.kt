package com.all.language.translate.voice.translator.common

import android.app.Activity
import com.all.language.translate.voice.translator.CustomApplication

val Activity.customApplication: CustomApplication
get() = application as CustomApplication