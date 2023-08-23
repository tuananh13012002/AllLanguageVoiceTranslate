package com.all.language.translate.voice.translator.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import com.all.language.translate.voice.translator.base.activities.BaseActivity
import com.all.language.translate.voice.translator.databinding.ActivitySplashBinding
import com.all.language.translate.voice.translator.ui.multi_lang.MultiLangActivity
import com.all.language.translate.voice.translator.ui.permission.PermissionAct
import com.all.language.translate.voice.translator.ui.utils.DeviceUtil
import com.all.language.translate.voice.translator.ui.utils.SystemUtil
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {
    @Inject
    lateinit var preferences: SharedPreferences
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SystemUtil.setPreLanguage(this,SystemUtil.getPreLanguage(this))
        SystemUtil.setLocale(this)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this) {}
        binding.imgLaunch.postDelayed({
            openMainActivity()
            SystemUtil.setPreLanguage(this,SystemUtil.getPreLanguage(this))
            SystemUtil.setLocale(this)
        }, 3000)
    }

    private fun restorePrefData(): Boolean {
        val pref = applicationContext.getSharedPreferences("myPref", MODE_PRIVATE)
        return pref.getBoolean("isIntroOpened", false)
    }

    private fun openMainActivity() {
        if (restorePrefData()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            startActivity(MultiLangActivity.getIntent(this, 1))
            finish()
        }
//        if (DeviceUtil.hasCameraPermission(this@SplashActivity)) {
//            if (restorePrefData()) {
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
//            } else {
//                startActivity(MultiLangActivity.getIntent(this, 1))
//                finish()
//            }
//        } else {
//            if (restorePrefData()) {
//                startActivity(Intent(this, PermissionAct::class.java))
//                finish()
//            } else {
//                startActivity(MultiLangActivity.getIntent(this, 1))
//                finish()
//            }
//        }
    }
}