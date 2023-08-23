package com.all.language.translate.voice.translator

import android.app.Application
import com.all.language.translate.voice.translator.ui.utils.SystemUtil
import com.appsflyer.AppsFlyerLib
import com.appsflyer.adrevenue.AppsFlyerAdRevenue
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class CustomApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppsFlyerLib.getInstance().init(this.getString(R.string.app_flyer), null, this)
        AppsFlyerLib.getInstance().start(this)

        val afRevenueBuilder = AppsFlyerAdRevenue.Builder(this)
        AppsFlyerAdRevenue.initialize(afRevenueBuilder.build())
    }

}