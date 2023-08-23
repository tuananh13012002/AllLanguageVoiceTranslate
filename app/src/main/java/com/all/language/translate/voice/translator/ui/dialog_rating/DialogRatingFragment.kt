package com.all.language.translate.voice.translator.ui.dialog_rating

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.all.language.translate.voice.translator.R


class DialogRatingFragment(private val context: Context) : Dialog(
    context, R.style.CustomAlertDialog
) {
    private var onPress: OnPress? = null
    private val tvTitle: TextView
    private val tvContent: TextView
    private val rtb: RatingBar
    private val imgIcon: ImageView
    private val imageView: ImageView
    private val editFeedback: EditText
    private val sharedPreference: SharedPreferences? = null
    private val editor: SharedPreferences.Editor? = null
    private val KEY_CHECK_OPEN_APP = "KEY CHECK OPEN APP"
    private val btnRate: Button
    private val Send: Button? = null
    private val Cancel: Button? = null
    private val btnLater: Button

    init {
        setContentView(R.layout.fragment_dialog_rating)
        val attributes = window!!.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = attributes
        window!!.setSoftInputMode(16)
        tvTitle = findViewById<View>(R.id.tvTitle) as TextView
        tvContent = findViewById<View>(R.id.tvContent) as TextView
        rtb = findViewById<View>(R.id.rtb) as RatingBar
        imgIcon = findViewById<View>(R.id.imgIcon) as ImageView
        imageView = findViewById<View>(R.id.imageView) as ImageView
        editFeedback = findViewById<View>(R.id.editFeedback) as EditText
        btnRate = findViewById<View>(R.id.btnRate) as Button
        btnLater = findViewById<View>(R.id.btnLater) as Button

        onclick()
        changeRating()

        setCancelable(false)

        editFeedback.visibility = View.GONE
        btnRate.text = getContext().getString(R.string.btn_rate)
        imgIcon.setImageResource(R.drawable.rating_default)
        tvTitle.text = getContext().getString(R.string.title_rate_0)
        tvContent.text = getContext().getString(R.string.content_rate_0)
        rtb.rating == 0f

    }

    interface OnPress {
        fun sendThank()
        fun rating()
        fun cancel()
        fun later()
    }

    fun init(context: Context?, onPress: OnPress?) {
        this.onPress = onPress
    }

    fun changeRating() {
        rtb.onRatingBarChangeListener =
            RatingBar.OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                val getRating = rtb.rating.toString()
                when (getRating) {
                    "1.0" -> {
                        editFeedback.visibility = View.GONE
                        btnRate.text = getContext().getString(R.string.btn_rate)
                        tvTitle.text = getContext().getString(R.string.title_rate_1_2_3)
                        tvContent.text = getContext().getString(R.string.content_rate_1_2_3)
                        imgIcon.setImageResource(R.drawable.rate_1)
                    }

                    "2.0" -> {
                        editFeedback.visibility = View.GONE
                        btnRate.text = getContext().getString(R.string.btn_rate)
                        tvTitle.text = getContext().getString(R.string.title_rate_1_2_3)
                        tvContent.text = getContext().getString(R.string.content_rate_1_2_3)
                        imgIcon.setImageResource(R.drawable.rate_2)
                    }

                    "3.0" -> {
                        editFeedback.visibility = View.GONE
                        btnRate.text = getContext().getString(R.string.btn_rate)
                        tvTitle.text = getContext().getString(R.string.title_rate_1_2_3)
                        tvContent.text = getContext().getString(R.string.content_rate_1_2_3)
                        imgIcon.setImageResource(R.drawable.rate_3)
                    }

                    "4.0" -> {
                        editFeedback.visibility = View.GONE
                        btnRate.text = getContext().getString(R.string.btn_rate)
                        tvTitle.text = getContext().getString(R.string.title_rate_4_5)
                        tvContent.text = getContext().getString(R.string.content_rate_4_5)
                        imgIcon.setImageResource(R.drawable.rate_4)
                    }

                    "5.0" -> {
                        editFeedback.visibility = View.GONE
                        btnRate.text = getContext().getString(R.string.btn_rate)
                        tvTitle.text = getContext().getString(R.string.title_rate_4_5)
                        tvContent.text = getContext().getString(R.string.content_rate_4_5)
                        imgIcon.setImageResource(R.drawable.rate_5)
                    }

                    else -> {
                        btnRate.text = getContext().getString(R.string.btn_rate)
                        editFeedback.visibility = View.GONE
                        tvTitle.text = getContext().getString(R.string.title_rate_0)
                        tvContent.text = getContext().getString(R.string.content_rate_0)
                        imgIcon.setImageResource(R.drawable.rating_default)
                    }
                }
            }
    }

    val newName: String
        get() = editFeedback.text.toString()
    val rating: String
        get() = rtb.rating.toString()

    fun onclick() {
        btnRate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                savePrefData()
                if (rtb.rating == 0f) {
                    Toast.makeText(
                        context, "Please feedback", Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (rtb.rating <= 4.0) {
                    imageView.visibility = View.GONE
                    imgIcon.visibility = View.VISIBLE
                    onPress!!.sendThank()
                } else {
                    //Edit
                    //imageView.setVisibility(View.VISIBLE);
                    imageView.visibility = View.GONE
                    imgIcon.visibility = View.VISIBLE
                    onPress!!.rating()
                }
            }
        })
        btnLater.setOnClickListener { onPress!!.later() }
    }
    private fun savePrefData() {
        val pref = context.getSharedPreferences("myPref", AppCompatActivity.MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.putBoolean("isRate", true)
        editor?.commit()
    }
}