package com.visualpro.dictionary.ui.translateScreen

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.speech.RecognizerIntent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.suke.widget.SwitchButton
import com.visualpro.dictionary.MainApplication
import com.visualpro.dictionary.R
import com.visualpro.dictionary.TapToTranslateService
import com.visualpro.dictionary.TapToTranslateService.Companion.CHANNEL_ID
import com.visualpro.dictionary.databinding.GGTranslateFragmentBinding
import com.visualpro.dictionary.viewmodel.GGTranslateViewModel
import java.util.*


class TranslateFragment : Fragment() {
    private val mHandler by lazy {
        Handler(Looper.getMainLooper())
    }
    private lateinit var binding: GGTranslateFragmentBinding
    private val viewModel: GGTranslateViewModel by viewModels {
        val application = requireActivity().application as MainApplication
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

    @SuppressLint("NewApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = GGTranslateFragmentBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    var canDrawOverlay =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Settings.canDrawOverlays(requireContext())) {
                binding.switchTaptotranslate.isChecked=true
                allowService(true)
            } else {
                checkDrawOverlayPermission()
            }

        }


    @RequiresApi(Build.VERSION_CODES.M)
    fun checkDrawOverlayPermission() {
        if (!Settings.canDrawOverlays(requireContext()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            showPermissionDialog()
            mHandler.post {
                binding.switchTaptotranslate.setOnCheckedChangeListener(null)
                binding.switchTaptotranslate.toggle(false)
                binding.switchTaptotranslate.setOnCheckedChangeListener(switchListen)
            }

        } else {
            allowService(true)

        }
    }

    fun allowService(isChecked: Boolean) {
        startFloatTranslateService(isChecked)
        viewModel.onTapTptranslateCofigChange(isChecked)

    }

    private fun showHowToUseDialog() {
        showHowToUse=false

        MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme).setTitle("Floating Translate")
            .setMessage("Floating Translate help you translate anywhere on screen by long click and select on the text which you want to translate!")
            .setPositiveButton("OK", null).show()
    }

    private fun showPermissionDialog() {
        MaterialAlertDialogBuilder(requireActivity(), R.style.AlertDialogTheme).setTitle("PERMISSION REQUIRE")
            .setMessage("This feature need the Displaying over other app permission, allow? ")
            .setPositiveButton("GRANT", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${requireContext().getPackageName()}")
                    ).apply {
                        canDrawOverlay.launch(this)
                    }
                }

            })
            .setNegativeButton("Cancel", null).show()




    }

    private var showHowToUse=false
    private var switchListen=  object : SwitchButton.OnCheckedChangeListener {
        override fun onCheckedChanged(view: SwitchButton?, isChecked: Boolean) {
            if (isChecked) {
                if(showHowToUse){
                    showHowToUseDialog()
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkDrawOverlayPermission()
                }else{
                    allowService(true)
                }
            } else {
                showHowToUse=true
                allowService(false)
            }
        }

    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun init() {
        checkIsSetUpLanguageSuccess()
        checkTapToTranslate()
        binding.switchTaptotranslate.setOnCheckedChangeListener(switchListen)
        binding.btnClear.setOnClickListener({
            viewModel.clearText()
            Toast.makeText(requireContext(), getString(R.string.clear), Toast.LENGTH_SHORT).show()
        })
        binding.btnRecord.setOnClickListener({
            recoding()
        })
        binding.btnCopy.setOnClickListener({
            TapToTranslateService.currentClipboardDataNeedToTranslate=false
            setTextToClipboard(binding.textOutput.text.toString())
            Toast.makeText(requireContext(), getString(R.string.copied), Toast.LENGTH_SHORT).show()

        })
        binding.textInput.setOnClickListener({
            var langIn = ""
            var langOut = ""
            if (viewModel.languageDetetectEnable.value == true) {
                langIn = getString(R.string.auto_detect)
            } else {
                langIn = viewModel.getLanguageInput()
            }
            langOut = viewModel.getLanguageOutput()
            val bottom = BottomTranslateFragment(binding.textInput.text.toString(), langIn, langOut)
            bottom.show(childFragmentManager, "")
        })
        binding.langSrc.setOnClickListener({
            val bottomDialog = BottomSelectLanguageFragment()
            bottomDialog.type = BottomSelectLanguageFragment.TYPE_TRANSLATE_FROM
            bottomDialog.show(childFragmentManager, "")
        })
        binding.langOut.setOnClickListener {
            val bottom = BottomSelectLanguageFragment()
            bottom.type = BottomSelectLanguageFragment.TYPE_TRANSLATE_TO
            bottom.show(childFragmentManager, "")
        }


        viewModel.translateResponse.observe(viewLifecycleOwner, {
            if (!it.textOut1.equals("")) {
                mHandler.postDelayed({
                    hideProgressBar()
                }, 800)
            }

            binding.textOutput.setText(it.textOut1)
            binding.textInput.setText(it.textIn1)
        })
        val listCountry = resources.getStringArray(R.array.countries_array)
        viewModel.indexInputLanguageSeleted.observe(viewLifecycleOwner, {
            if (viewModel.languageDetetectEnable.value == true) {
                binding.LangIn.setText(getString(R.string.auto_detect))
                binding.textInput.setHint(getString(R.string.translate_from_auto))
            } else {
                binding.textInput.setHint(getString(R.string.translate_from, listCountry[it]))
                binding.LangIn.text = listCountry[it]
            }

        })
        viewModel.languageDetetectEnable.observe(viewLifecycleOwner, {
            if (it) {
                binding.LangIn.setText(getString(R.string.auto_detect))
                binding.textInput.setHint(getString(R.string.translate_from_auto))
            } else {
                val index = viewModel.indexInputLanguageSeleted.value!!
                binding.LangIn.setText(listCountry[index])
                binding.textInput.setHint(getString(R.string.translate_from, listCountry[index]))
            }
        })
        viewModel.indexOutputSelected.observe(viewLifecycleOwner, {
            binding.textOutput.hint = getString(R.string.to, listCountry[it])
            binding.textOut.text = listCountry[it]
        })

    }

    private fun checkTapToTranslate() {
        viewModel.checkTapTranslateEnable {
            Log.d("test", "checkTapToTranslate: $it")
            binding.switchTaptotranslate.isChecked = it

        }
    }
    private var isBound = false
    private lateinit var mService: TapToTranslateService
    private var conn = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val mBinder = p1 as TapToTranslateService.mBinder
            mService = mBinder.getInstance()
            viewModel.setServiceInstance(mService)
            isBound = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isBound = false
            Log.d("test", "onServiceDisConnected: ")
        }

    }


    private fun startFloatTranslateService(checked: Boolean) {
//        viewModel.onTapTptranslateCofigChange(c)
        if (checked) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = android.app.NotificationChannel(
                    CHANNEL_ID, "Float service", NotificationManager.IMPORTANCE_DEFAULT
                )
                val manager = requireActivity().getSystemService(NotificationManager::class.java)
                manager.createNotificationChannel(channel)
            }


            Intent(requireContext(), TapToTranslateService()::class.java).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requireContext().startForegroundService(this)
                } else {
                    requireContext().startService(this)
                }
                requireContext().bindService(this, conn, Context.BIND_AUTO_CREATE)
            }
//            }

        } else {
            mService.hideNotification()
            mService.stopSelf()
            requireContext().unbindService(conn)

        }
    }


    private fun checkIsSetUpLanguageSuccess() {
        viewModel.isFirstSetUpLanguageSucess { isDone ->
            if (!isDone) {
                val dialog = FirstSelectLanguage()
                dialog.show(childFragmentManager, "")
            }
        }
    }


    fun setTextToClipboard(text: String) {
        val clipboard = requireActivity().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
        clipboard?.let {
            it.text = text
        }
    }

    fun showProgressBar() {
        binding.progress.show()
        binding.textOutput.visibility = View.GONE
    }

    fun hideProgressBar() {
        binding.progress.hide()
        binding.textOutput.visibility = View.VISIBLE
    }

    override fun onStop() {
        super.onStop()
        mHandler.removeCallbacksAndMessages(null)
    }

    var getStringFromVoiceSearch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.getSerializableExtra(RecognizerIntent.EXTRA_RESULTS)?.let {
                val voiceText = (it as ArrayList<*>).get(0).toString()
                binding.textInput.setText(voiceText)
                viewModel.translate(voiceText)
                showProgressBar()


            }
        }


    fun recoding() {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "")
            try {

                getStringFromVoiceSearch.launch(this)
            } catch (e: ActivityNotFoundException) {
                MaterialAlertDialogBuilder( requireContext(),R.style.AlertDialogTheme).apply {
                    setTitle("Google+ require")
                    setMessage("Text recognize working with Google+ application, do you want to download from Google Play store now?" )
                    setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            val appPackageName = "com.google.android.googlequicksearchbox"
                            try {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")
                                    )
                                )
                            } catch (anfe: ActivityNotFoundException) {
                                startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                                    )
                                )
                            }
                        }

                    })
                    setNegativeButton("Cancel",null)
                    show()
                }
            }

        }
    }
}