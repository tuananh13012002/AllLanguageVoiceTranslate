package com.all.language.translate.voice.translator.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.all.language.translate.voice.translator.R
import com.all.language.translate.voice.translator.base.activities.BaseActivity
import com.all.language.translate.voice.translator.databinding.ActivityMainBinding
import com.all.language.translate.voice.translator.ui.dialog_rating.DialogRatingFragment
import com.all.language.translate.voice.translator.ui.dialog_rating.SharePrefUtils
import com.all.language.translate.voice.translator.ui.home.HomeFragment
import com.all.language.translate.voice.translator.ui.multi_lang.MultiLangActivity
import com.all.language.translate.voice.translator.ui.speak.SpeakFragment
import com.all.language.translate.voice.translator.ui.text.TextFragment
import com.all.language.translate.voice.translator.ui.utils.SystemUtil
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : BaseActivity() {

    @Inject
    lateinit var preferences: SharedPreferences
    private var loadingLayout: FrameLayout? = null
    private lateinit var binding: ActivityMainBinding
    private var manager: ReviewManager? = null
    private var reviewInfo: ReviewInfo? = null
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var ratingDialog: DialogRatingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout,
            R.string.string_home, R.string.string_home
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        showMenuLeft()
        binding.menuBtn.setOnClickListener {
            binding.drawerLayout.openDrawer(binding.nav)
            hideKeyboard()
        }
        replaceFragment(HomeFragment())
        savePrefData()

    }

    private fun showMenuLeft() {
        binding.nav.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                }

                R.id.text_translate -> {
                    replaceFragment(TextFragment())
                }

                R.id.voice_translate -> {
                    replaceFragment(SpeakFragment())
                }

                R.id.change_language -> {
                    startActivity(MultiLangActivity.getIntent(this, 2))
                    finish()
                }

                R.id.rate -> {
                    if(restorePrefData() == true){
                        Toast.makeText(this, "You have already rated", Toast.LENGTH_SHORT).show()
                    }else{
                        showDialogRate()
                    }
                }

                R.id.privacy -> {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://biuustudio.netlify.app/policy")
                    )
                    startActivity(intent)
                }
            }
            binding.drawerLayout.closeDrawer(binding.nav)
            true
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        when (fragment) {
            is HomeFragment -> binding.title.text = getString(R.string.string_home)
            is TextFragment -> binding.title.text = getString(R.string.title_text_translation)
            is SpeakFragment -> binding.title.text = getString(R.string.title_voice_translation)
        }
    }

    override fun showLoading(isShow: Boolean) {
        loadingLayout?.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun savePrefData() {
        val pref = applicationContext.getSharedPreferences("myPref", MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean("isIntroOpened", true)
        editor.commit()
    }

    private fun showDialogRate() {
        ratingDialog = DialogRatingFragment(this)
        ratingDialog.init(
            this,
            object : DialogRatingFragment.OnPress {
                override fun sendThank() {
                    SharePrefUtils.forceRated(
                        this@MainActivity
                    )
                    Toast.makeText(
                        this@MainActivity,
                        getString(R.string.string_thank_you_for_rating_the_app),
                        Toast.LENGTH_SHORT
                    ).show()

                    ratingDialog.dismiss()

//                    binding.llRateUs.visibility = View.GONE
                }

                override fun rating() {
                    manager = ReviewManagerFactory.create(this@MainActivity)
                    val request: Task<ReviewInfo> = manager!!.requestReviewFlow()
                    request.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            reviewInfo = task.result
                            val flow: Task<Void> =
                                manager!!.launchReviewFlow(this@MainActivity, reviewInfo!!)
                            flow.addOnSuccessListener {
                                SharePrefUtils.forceRated(
                                    this@MainActivity
                                )
                                ratingDialog.dismiss()


                            }
                        } else {
                            ratingDialog.dismiss()

                        }
                    }
                }

                override fun cancel() {}
                override fun later() {
                    ratingDialog.dismiss()
                }
            }
        )
        try {
            ratingDialog.show()
        } catch (e: WindowManager.BadTokenException) {
            e.printStackTrace()
        }
        ratingDialog.setOnKeyListener { dialog, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_UP) {
                dialog.dismiss()
            }
            true
        }
    }
    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (currentFocus != null) {
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
    private fun restorePrefData(): Boolean? {
        val pref = getSharedPreferences("myPref", AppCompatActivity.MODE_PRIVATE)
        return pref?.getBoolean("isRate", false)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(binding.nav)) {
            binding.drawerLayout.closeDrawer(binding.nav)
        } else {
            super.onBackPressed()
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}