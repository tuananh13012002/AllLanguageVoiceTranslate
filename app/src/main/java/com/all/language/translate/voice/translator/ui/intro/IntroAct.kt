package com.all.language.translate.voice.translator.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.all.language.translate.voice.translator.R
import com.all.language.translate.voice.translator.base.activities.BaseActivity
import com.all.language.translate.voice.translator.databinding.FragmentIntroBinding
import com.all.language.translate.voice.translator.ui.adapters.CarouselAdapter
import com.all.language.translate.voice.translator.ui.permission.PermissionAct

class IntroAct : BaseActivity() {
    private lateinit var binding: FragmentIntroBinding
    private lateinit var carouselAdapter: CarouselAdapter
    var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val carouselItems = listOf(
            Triple(
                R.drawable.intro_1,
                getString(R.string.intro_title_1),
                getString(R.string.intro_content_1)
            ),
            Triple(
                R.drawable.intro_2,
                getString(R.string.intro_title_2),
                getString(R.string.intro_content_2)
            ),
            Triple(
                R.drawable.intro_3,
                getString(R.string.intro_title_3),
                getString(R.string.intro_content_3)
            )
        )

        carouselAdapter = CarouselAdapter(carouselItems)
        var dotsIndicator = binding.dotsIndicator
        var viewPager = binding.viewPager
        var btnStart = binding.buttonStartIntro

        viewPager.adapter = carouselAdapter
        dotsIndicator.setViewPager2(viewPager)

        btnStart.setOnClickListener {
            position = viewPager.currentItem
            if (position < carouselItems.size) {
                position++
                viewPager.currentItem = position
            }
            if (position == carouselItems.size) {
                startActivity(Intent(this, PermissionAct::class.java))
                finish()
            }
        }
    }
}