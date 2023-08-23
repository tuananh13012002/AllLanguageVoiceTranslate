package com.all.language.translate.voice.translator.ui.text

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.setViewTreeOnBackPressedDispatcherOwner
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import com.all.language.translate.voice.translator.activities.MainActivity
import com.all.language.translate.voice.translator.base.fragment.BaseFragment
import com.all.language.translate.voice.translator.data.models.SingletonLanguageTrans
import com.all.language.translate.voice.translator.databinding.FragmentTextBinding
import com.all.language.translate.voice.translator.ui.home.HomeFragment
import com.all.language.translate.voice.translator.ui.lang.LanguageTransAct
import com.all.language.translate.voice.translator.ui.speak.SpeakFragment
import com.all.language.translate.voice.translator.ui.utils.CustomEdittext
import com.all.language.translate.voice.translator.ui.utils.SystemUtil
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TextFragment : BaseFragment() {
    private lateinit var dataBinding: FragmentTextBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = FragmentTextBinding.inflate(inflater)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        setOnBackPressed()
        return dataBinding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        dataBinding.mainLayout.setOnClickListener {
            inputMethodManager.hideSoftInputFromWindow(dataBinding.idTVInput.windowToken, 0)
            activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        // click icon x clean text render voice
        dataBinding.btnClearEdt.setOnClickListener {
            dataBinding.idTVInput.setText("")
            dataBinding.idTVOutput.setText("")
        }


        dataBinding.btnSwapLang.setOnClickListener {
            SingletonLanguageTrans.getInstance().swapLang()
            changeLanguageHome()
        }

        // click icon copy handle
        dataBinding.copyButton.setOnClickListener {
            if (dataBinding.idTVInput.text.toString().trim().isNotEmpty()) {
                // Create a new ClipData with the text
                val clipData = ClipData.newPlainText("text", dataBinding.idTVInput.text.toString())

                // Set the ClipData to the clipboard
                clipboardManager.setPrimaryClip(clipData)

                // Show a message or perform any other actions after copying
                showToast("Text copied to clipboard!") //TODO put text to strings.xml
            } else {
                showToast("Nothing to copy.")
            }
        }
        dataBinding.btnCopyOutput.setOnClickListener {
            if (dataBinding.idTVOutput.text.toString().trim().isNotEmpty()) {
                // Create a new ClipData with the text
                val clipData = ClipData.newPlainText("text", dataBinding.idTVOutput.text.toString())

                // Set the ClipData to the clipboard
                clipboardManager.setPrimaryClip(clipData)

                // Show a message or perform any other actions after copying
                showToast("Text copied to clipboard!") //TODO put text to strings.xml
            } else {
                showToast("Nothing to copy.")
            }
        }

        changeLanguageHome()

        dataBinding.tapToTranButton.setOnClickListener {
            dataBinding.progress.isVisible = true
            dataBinding.layout.visibility=View.GONE
            performTranslation()
        }

        dataBinding.btnVoice.setOnClickListener {
            (activity as MainActivity).replaceFragment(SpeakFragment())
        }
        dataBinding.idTVInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    dataBinding.btnClearEdt.visibility = View.VISIBLE
                    dataBinding.copyButton.visibility = View.VISIBLE
                } else {
                    dataBinding.btnClearEdt.visibility = View.GONE
                    dataBinding.copyButton.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        dataBinding.idTVOutput.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    dataBinding.btnCopyOutput.visibility = View.VISIBLE
                } else {
                    dataBinding.btnCopyOutput.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        dataBinding.idTVInput.requestFocus()
        dataBinding.idTVOutput.requestFocus()
        dataBinding.idTVInput.setKeyImageChange { keyCode, keyEvent ->
            if (keyEvent?.keyCode == 4) {
                activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            }
        }
    }

    private fun changeLanguageHome() {
        dataBinding.langFrom.text = SingletonLanguageTrans.getInstance().langTransFrom.name
        dataBinding.tvNameFromLang.text = SingletonLanguageTrans.getInstance().langTransFrom.name
        dataBinding.langTo.text = SingletonLanguageTrans.getInstance().langTransTo.name
        dataBinding.tvNameToLang.text = SingletonLanguageTrans.getInstance().langTransTo.name
        dataBinding.imgLangFrom.setImageResource(
            SingletonLanguageTrans.getInstance().langTransFrom.avatar ?: 0
        )
        dataBinding.imgToFrom.setImageResource(
            SingletonLanguageTrans.getInstance().langTransTo.avatar ?: 0
        )

        dataBinding.formLang.setOnClickListener {
            startForResult.launch(LanguageTransAct.getIntent(requireContext(), "from"))
        }
        dataBinding.toLang.setOnClickListener {
            startForResult.launch(LanguageTransAct.getIntent(requireContext(), "to"))
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
            } else {
                // Handle accordingly
            }
        }

    override fun onResume() {
        super.onResume()
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        changeLanguageHome()
    }

    private fun performTranslation() {
        //TODO refactoring this function, put options, conditions out of this function, only call translate here
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(SingletonLanguageTrans.getInstance().langTransFrom.code ?: "")
            .setTargetLanguage(SingletonLanguageTrans.getInstance().langTransTo.code ?: "")
            .build()
        val fromToLangTranslator = Translation.getClient(options)

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        fromToLangTranslator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                fromToLangTranslator.translate(dataBinding.idTVInput.text.toString())
                    .addOnSuccessListener { translatedText ->
                        dataBinding.progress.isVisible = false
                        dataBinding.layout.visibility=View.VISIBLE
                        dataBinding.idTVOutput.setText(translatedText.toString())

                    }
            }
            .addOnFailureListener { exception ->
            }
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
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

    override fun onPause() {
        super.onPause()
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    override fun onStop() {
        super.onStop()
        activity?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }

    companion object {
        private const val REQUEST_WRITE_MESSAGE = 100

        fun getIntent(context: Context): Intent {
            return Intent(context, TextFragment::class.java)
        }
    }
}