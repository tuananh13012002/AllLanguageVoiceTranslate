package com.all.language.translate.voice.translator.ui.setting

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.all.language.translate.voice.translator.R
import com.all.language.translate.voice.translator.activities.MainActivity
import com.all.language.translate.voice.translator.base.fragment.BaseFragment
import com.all.language.translate.voice.translator.databinding.FragmentSettingBinding
import com.all.language.translate.voice.translator.ui.dialog_rating.DialogRatingFragment
import com.all.language.translate.voice.translator.ui.dialog_rating.SharePrefUtils
import com.all.language.translate.voice.translator.ui.home.HomeFragment
import com.all.language.translate.voice.translator.ui.multi_lang.MultiLangActivity
import com.all.language.translate.voice.translator.ui.speak.SpeakFragment
import com.all.language.translate.voice.translator.ui.utils.SystemUtil
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.android.play.core.tasks.Task

class SettingsFragment : BaseFragment() {
    private lateinit var binding: FragmentSettingBinding
    //Rate
    private var manager: ReviewManager? = null
    private var reviewInfo: ReviewInfo? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentSettingBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        setOnBackPressed()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAction()
    }

    private fun initAction() {
        binding.backBtn.setOnClickListener {
            (activity as MainActivity).replaceFragment(HomeFragment())
        }

        binding.llPrivacyPolicy.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://biuustudio.netlify.app/policy"))
            startActivity(intent)
        }

        binding.llRateUs.setOnClickListener {
            if(restorePrefData() == true){
                Toast.makeText(context, "You have already rated", Toast.LENGTH_SHORT).show()
            }else{
                showDialogRate()
            }
        }
        binding.llLanguage.setOnClickListener {
//            startActivity(MultiLangActivity.getIntent(requireContext(), false))
            activity?.finish()
        }
    }
    private fun restorePrefData(): Boolean? {
        val pref = context?.getSharedPreferences("myPref", AppCompatActivity.MODE_PRIVATE)
        return pref?.getBoolean("isRate", false)
    }

    private fun showDialogRate() {
        val ratingDialog = DialogRatingFragment(requireActivity())
        ratingDialog.init(
            requireActivity(),
            object : DialogRatingFragment.OnPress {
                override fun sendThank() {
                    SharePrefUtils.forceRated(
                        requireActivity()
                    )
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.string_thank_you_for_rating_the_app),
                        Toast.LENGTH_SHORT
                    ).show()

                    ratingDialog.dismiss()

//                    binding.llRateUs.visibility = View.GONE
                }

                override fun rating() {
                    manager = ReviewManagerFactory.create(requireActivity())
                    val request: Task<ReviewInfo> = manager!!.requestReviewFlow()
                    request.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            reviewInfo = task.result
                            val flow: Task<Void> =
                                manager!!.launchReviewFlow(requireActivity(), reviewInfo!!)
                            flow.addOnSuccessListener {
                                SharePrefUtils.forceRated(
                                    requireActivity()
                                )
                                ratingDialog.dismiss()

//                                binding.llRateUs.visibility = View.GONE

                            }
                        } else {
                            ratingDialog.dismiss()

//                            binding.llRateUs.visibility = View.GONE
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
    }
    private fun setOnBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(isEnabled){
                    isEnabled=false
                    SystemUtil.setPreLanguage(requireContext(), SystemUtil.getPreLanguage(requireContext()))
                    SystemUtil.setLocale(requireContext())
                }
            }
        })
    }

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SettingsFragment::class.java)
        }
    }
}