package com.visualpro.realproject.ui.dictionary_screen

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.visualpro.realproject.MainApplication
import com.visualpro.realproject.R
import com.visualpro.realproject.adapter.RecentAdapter
import com.visualpro.realproject.adapter.SearchAdapter
import com.visualpro.realproject.databinding.DictionaryFragmentBinding
import com.visualpro.realproject.ui.MainActivity2
import com.visualpro.realproject.viewmodel.DictionaryViewModel
import com.visualpro.realproject.viewmodel.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import java.util.*


class DictionaryFragment : Fragment() {
    private lateinit var mRecentAdapter: RecentAdapter
    private lateinit var bind: DictionaryFragmentBinding
    private lateinit var searchAdapter: SearchAdapter
    private var coroutine = CoroutineScope(IO)
    private val handler = Handler(Looper.myLooper()!!)
    private val mViewModel: DictionaryViewModel by viewModels {
        DictionaryViewModel.DictionaryViewModelFactory(
            (requireActivity().application as MainApplication).repository
        )
    }
    private var viewModelShare: MainActivityViewModel? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModelShare = (requireActivity() as MainActivity2).mViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        bind = DictionaryFragmentBinding.inflate(inflater, container, false)
        init()
        initAdapter()
        return bind.root
    }

    private fun initAdapter() {
            searchAdapter = SearchAdapter()
            searchAdapter.listenerClick = {
                navigationToDefinition(searchAdapter.mList[it].searchText!!)
                bind.edttt.setText("")
                bind.scrollView.visibility= VISIBLE
                bind.frame.visibility= GONE

            }
            bind.rcvHint.adapter = searchAdapter
            bind.rcvHint.layoutManager = LinearLayoutManager(requireContext())
            bind.rcvHint.setHasFixedSize(false)


            mRecentAdapter = RecentAdapter()
            mRecentAdapter.onClick= {
                navigationToDefinition(mRecentAdapter.mList?.get(it)?.word!!)}
            bind.rcvRecent.adapter = mRecentAdapter
            bind.rcvRecent.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

    }

    fun hideKeyboard() {
        val imm =
            requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(bind.edttt.windowToken, 0)
    }

    private fun navigationToDefinition(word:String){
        (requireActivity() as MainActivity2).binding.bottomNavigationView.selectTabAt(3)
        viewModelShare!!.beginAnition.value=true
        viewModelShare!!.getWordFromServer(word, "", false)
    }
    private fun init() {
        initObservers()

        (requireActivity() as MainActivity2).binding.toolbar.setNavigationOnClickListener {
                val animation=AnimationUtils.loadAnimation(requireContext(), R.anim.apear_left_to_right)
                bind.scrollView.startAnimation(animation)
                Handler(Looper.getMainLooper()).postDelayed({
                    bind.frame.visibility =GONE
                    bind.imageView.setImageResource(R.drawable.aic_microphone)
                    bind.scrollView.visibility =VISIBLE
                   bind.edttt.clearFocus()
                    hideKeyboard()
                }, 495)


        }


        bind.btnSpeaker.setOnClickListener {
            viewModelShare!!.playSoundUs()
        }
        bind.btnShowdailyword.setOnClickListener {
            navigationToDefinition(mViewModel.DailyWord.value!!.word)
        }
        bind.imageView.setOnClickListener({
            if(bind.frame.visibility== VISIBLE){
                bind.edttt.setText("")
            }else{
                recoding()
            }

        })

        mViewModel.checkDailyWord()
        bind.edttt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0!!.length > 1) {
                    viewModelShare!!.querySeach(p0.toString())
                    bind.progressbar.visibility = VISIBLE
                    bind.rcvHint.visibility = GONE
                } else {
                    searchAdapter.mList.clear()
                    searchAdapter.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        bind.edttt.setOnFocusChangeListener({ _, b ->
            if (b) {
                val animation=AnimationUtils.loadAnimation(requireContext(), R.anim.fade_right_to_left)
                bind.scrollView.startAnimation(animation)
                Handler(Looper.getMainLooper()).postDelayed({
                    bind.scrollView.visibility =GONE
                    bind.frame.visibility =VISIBLE
                    bind.imageView.setImageResource(R.drawable.aic_close12)
                }, 495)
                (requireActivity() as MainActivity2).setNavigationMenuIcon(R.drawable.ic_back)

            } else {
               hideKeyboard()
                (requireActivity() as MainActivity2).setNavigationMenuIcon(null)
                bind.imageView.setImageResource(R.drawable.aic_microphone)
            }
        })


    }

    private fun initObservers() {
        viewModelShare!!.searchItem_Response.observe(viewLifecycleOwner, {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                bind.progressbar.visibility = GONE
                bind.rcvHint.visibility = VISIBLE

            }, 600)
            searchAdapter.mList = it
            searchAdapter.notifyDataSetChanged()
        })

        mViewModel.DailyWord.observe(viewLifecycleOwner, {
            if (it != null) {
                bind.txtWordOfDay.setText(it.word)
                bind.txtDayOfWordOfDay.setText(it.dayOfWord)
                bind.txtDescriptionWordOfDay.setText(it.definition)
            }

        })

        mViewModel.mRecentItems.observe(viewLifecycleOwner,{
            mRecentAdapter.mList = it
            mRecentAdapter.notifyDataSetChanged()
        })
    }

    var getStringFromVoiceSearch = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        it.data?.getSerializableExtra(RecognizerIntent.EXTRA_RESULTS)?.let {
            bind.edttt.requestFocus()
            bind.edttt.setText((it as ArrayList<*>).get(0).toString())

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

                val dialog = MaterialAlertDialogBuilder( requireContext(),R.style.AlertDialogTheme).apply {
                    setTitle("Google+ require")
                    setMessage("Text recognize is a work with Google+ application, got it from Play Store now")
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

