package com.all.language.translate.voice.translator.ui.speak

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.all.language.translate.voice.translator.R
import com.all.language.translate.voice.translator.activities.MainActivity
import com.all.language.translate.voice.translator.base.fragment.BaseFragment
import com.all.language.translate.voice.translator.data.models.SingletonLanguageTrans
import com.all.language.translate.voice.translator.databinding.FragmentSpeakBinding
import com.all.language.translate.voice.translator.ui.home.HomeFragment
import com.all.language.translate.voice.translator.ui.lang.LanguageTransAct
import com.all.language.translate.voice.translator.ui.text.TextFragment
import com.all.language.translate.voice.translator.ui.utils.SystemUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.Objects

@AndroidEntryPoint
class SpeakFragment : BaseFragment() {
    private lateinit var binding: FragmentSpeakBinding

    private lateinit var codeFlag: String
    private val REQUEST_CODE_SPEECH_INPUT = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpeakBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        setOnBackPressed()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initAction()
    }

    private fun initAction() {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        // click icon x clean text render voice
        binding.btnClearEdt.setOnClickListener {
            binding.idTVOutput.setText("")
        }

        // click icon copy handle
        binding.copyButton.setOnClickListener {
            val textCopy = binding.idTVOutput.text.toString().trim()

            if (textCopy.isNotEmpty()) {
                // Create a new ClipData with the text
                val clipData = ClipData.newPlainText("text", textCopy)

                // Set the ClipData to the clipboard
                clipboardManager.setPrimaryClip(clipData)

                // Show a message or perform any other actions after copying
                showToast("Text copied to clipboard!")
            } else {
                showToast("Nothing to copy.")
            }
        }

        // listener for mic image view.
        binding.idIVMic.setOnClickListener {
            // on below line we are calling speech recognizer intent.
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)

            // on below line we are passing language model
            // and model free form in our intent
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            // on below line we are passing our
            // language as a default language.
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,
//                Locale.getDefault()
                codeFlag.toString()
            )

            // on below line we are specifying a prompt
            // message as speak to text on below line.
            intent.putExtra(
                RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.title_voice_translation)
            )

            // on below line we are specifying a try catch block.
            // in this block we are calling a start activity
            // for result method and passing our result code.
            try {
                startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                // todo
            }
        }

        binding.btnLang.setOnClickListener {
            startForResult.launch(LanguageTransAct.getIntent(requireContext(), "from"))
        }

        binding.btnText.setOnClickListener {
            (activity as MainActivity).replaceFragment(TextFragment())
        }
        binding.idTVOutput.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    binding.btnCopy.visibility = View.VISIBLE
                } else {
                    binding.btnCopy.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

    }

    private fun initData() {
        changeLanguageHome()
    }

    private fun changeLanguageHome() {
        binding.tvLang.text = SingletonLanguageTrans.getInstance().langTransFrom.name
        codeFlag = SingletonLanguageTrans.getInstance().langTransFrom.code ?: ""
        binding.imgLang.setImageResource(
            SingletonLanguageTrans.getInstance().langTransFrom.avatar ?: 0
        )
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
            } else {
                // Handle accordingly
            }
        }
    private fun setOnBackPressed(){
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                if(isEnabled){
                    isEnabled=false
                    (activity as MainActivity).replaceFragment(HomeFragment())
                    SystemUtil.setPreLanguage(requireContext(), SystemUtil.getPreLanguage(requireContext()))
                    SystemUtil.setLocale(requireContext())
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        changeLanguageHome()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // in this method we are checking request
        // code with our result code.
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            // on below line we are checking if result code is ok
            if (resultCode == Activity.RESULT_OK && data != null) {

                // in that case we are extracting the
                // data from our array list
                val res: ArrayList<String> =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                // on below line we are setting data
                // to our output text view.
                binding.idTVOutput.setText(Objects.requireNonNull(res)[0])
            }
        }
    }
}