package com.visualpro.dictionary.ui.float_translate

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.visualpro.dictionary.MainApplication
import com.visualpro.dictionary.R
import com.visualpro.dictionary.TapToTranslateService
import com.visualpro.dictionary.TapToTranslateService.Companion.DATA_CLIPBOARD
import com.visualpro.dictionary.TapToTranslateService.Companion.SHOW_FLOAT_ICON
import com.visualpro.dictionary.databinding.ActivityFloatTranslateBinding
import com.visualpro.dictionary.viewmodel.GGTranslateViewModel


class FloatTranslate : AppCompatActivity() {
    companion object {
        var isStarted = false
        const val ACTION_COPY = "action copy"
        const val NAVIGATION_TO_TRANSLATE_SCREEN = "nav translate screen"
    }

    private val mViewModel: GGTranslateViewModel by viewModels {
        val application = application as MainApplication
        GGTranslateViewModel.GGTranslateViewModelFactory(
            application.repository,
            application.sharePreference,
            application.resources.getStringArray(
                R.array.countries_array
            ),
            application.resources.getStringArray(
                R.array.countries_entry
            )
        )

    }
    private lateinit var binding: ActivityFloatTranslateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Transparent)
        super.onCreate(savedInstanceState)
        setWindowParams()
        binding = ActivityFloatTranslateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCopyFloat.setOnClickListener {
            Toast.makeText(this@FloatTranslate,"Copied", Toast.LENGTH_SHORT).show()
            Intent(this, TapToTranslateService::class.java).apply {
                putExtra(ACTION_COPY, binding.edtTranslate.text.toString())
                startService(this)
            }
        }
        binding.btnSpeakerFloat.setOnClickListener {
            Toast.makeText(baseContext, "Coming soon!", Toast.LENGTH_SHORT).show()
        }

        val list = resources.getStringArray(R.array.countries_array)
//        val list2 = ArrayList<String>()
//
//        list2.addAll(list)
//        list2.add(getString(R.string.auto_detect_recommend))
//
////        val listEntry = resources.getStringArray(R.array.countries_entry)
////        val systemLanguage = Locale.getDefault().getLanguage()
//
//        val adapterIn = ArrayAdapter(this, android.R.layout.simple_spinner_item, list2)
//        val adapterOut = ArrayAdapter(this, android.R.layout.simple_spinner_item, list2)
//
//        adapterOut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        adapterIn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        var intent = intent
        onNewIntent(intent)


        if(mViewModel.languageDetetectEnable.value==true){
            binding.spinnerLanguage.setText("Auto detect")
        }else{
            binding.spinnerLanguage.setText(mViewModel.getLanguageInput())
        }

        binding.button.setOnClickListener {
            binding.progressbar2.showNow()

//            ObjectAnimator.ofFloat(it, "translationX", 100f).apply {
//                duration = 2000
//                start()
//            }

            mViewModel.translate(binding.edtTranslate.text.toString())
            binding.spinnerLanguage.setText(mViewModel.getLanguageOutput())
        }

        binding.edtTranslate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0?.let {
                    if (it.length > 0) {
                        mViewModel.textTemp = it.toString()
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })


        mViewModel.translateResponse.observe(this, {
            Handler(Looper.getMainLooper()).postDelayed({
                binding.progressbar2.hide()
            }, 1000)
            binding.edtTranslate.setText(it.textOut1)
        })

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            mViewModel.textTemp = it.getStringExtra(DATA_CLIPBOARD).toString()
            binding.edtTranslate.setText(mViewModel.textTemp)
        }
    }

    override fun onResume() {
        super.onResume()
        isStarted = true
    }


    override fun onDestroy() {
        super.onDestroy()
        isStarted = false
        Intent(this, TapToTranslateService()::class.java).apply {
            putExtra(SHOW_FLOAT_ICON, "")
            startService(this)
        }
    }


    private fun setWindowParams() {
        window.attributes.apply {
            gravity = Gravity.TOP
            windowAnimations = R.style.animationAialogActivity
            window.attributes = this@apply

        }
    }

}