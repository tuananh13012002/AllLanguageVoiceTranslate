package com.all.language.translate.voice.translator.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.all.language.translate.voice.translator.R
import com.all.language.translate.voice.translator.activities.MainActivity
import com.all.language.translate.voice.translator.base.fragment.BaseFragment
import com.all.language.translate.voice.translator.databinding.FragmentHomeBinding
import com.all.language.translate.voice.translator.ui.speak.SpeakFragment
import com.all.language.translate.voice.translator.ui.text.TextFragment
import com.all.language.translate.voice.translator.ui.utils.SystemUtil
import kotlin.random.Random

class HomeFragment: BaseFragment() {
   private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        setOnBackPressed()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAction()
    }

    private fun initAction() {
        binding.textBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(TextFragment())
        }
        binding.voiceBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(SpeakFragment())
        }
        setRandomBanner()
    }
    private fun setRandomBanner() {
        val randomIndex = Random.nextInt(drawableList.size)
        val randomDrawableId = drawableList[randomIndex]
        binding.randomBanner.setImageResource(randomDrawableId)
    }
    private fun setOnBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(isEnabled){
                    isEnabled=false
//                    activity?.let {
//                        (activity as MainActivity).replaceFragment(HomeFragment())
//                    }
                    activity?.let {
                       it.finish()
                    }
                    SystemUtil.setPreLanguage(requireContext(), SystemUtil.getPreLanguage(requireContext()))
                    SystemUtil.setLocale(requireContext())
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        SystemUtil.setPreLanguage(requireContext(), SystemUtil.getPreLanguage(requireContext()))
        SystemUtil.setLocale(requireContext())
        setRandomBanner()
    }
    companion object{
         val drawableList = arrayOf(
            R.drawable.home_image_one_v1,
            R.drawable.home_image_two,
            R.drawable.home_image_three,
        )
    }
}