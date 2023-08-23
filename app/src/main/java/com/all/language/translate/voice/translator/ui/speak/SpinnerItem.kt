package com.example.speaktotext.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class SpinnerItem(val id: Int, val flag: String, val name: String, val code: String)

fun readDataFromJsonFile(context: Context): List<SpinnerItem> {
    val jsonString = context.assets.open("countries.json").bufferedReader().use { it.readText() }
    val gson = Gson()
    val itemType = object : TypeToken<List<SpinnerItem>>() {}.type
    return gson.fromJson(jsonString, itemType)
}