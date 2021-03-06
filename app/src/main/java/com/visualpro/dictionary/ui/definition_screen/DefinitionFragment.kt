package com.visualpro.dictionary.ui.definition_screen

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.visualpro.dictionary.R
import com.visualpro.dictionary._interface.onRequestSaveCurrentWord
import com.visualpro.dictionary.adapter.AdapterInterfaces.onItemRecyclerViewCkick
import com.visualpro.dictionary.adapter.DefinitionAdapter
import com.visualpro.dictionary.adapter.NearByWordAdapter
import com.visualpro.dictionary.databinding.DefinitionFragmentBinding
import com.visualpro.dictionary.model.WordTypeSeparate
import com.visualpro.dictionary.ui.MainActivity2
import com.visualpro.dictionary.ui.views_custom.AddToFavoriteDialog
import com.visualpro.dictionary.viewmodel.MainActivityViewModel
import com.visualpro.myapplication.Model.Definition


class DefinitionFragment : Fragment(), onItemRecyclerViewCkick, onRequestSaveCurrentWord {

    private lateinit var mViewModel: MainActivityViewModel

    private lateinit var binding: DefinitionFragmentBinding
    private lateinit var mNearByWordAdapter: NearByWordAdapter
    private lateinit var mDefinitionAdapter: DefinitionAdapter
    private var dialog: AddToFavoriteDialog? = null

//    private lateinit var mViewBottomDialog: View
//    private lateinit var mGoButton_NearByWord: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DefinitionFragmentBinding.inflate(layoutInflater)
        initView()
        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mViewModel = ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
        (requireActivity() as MainActivity2).callBackSaveCurrentWord = this
    }


    fun saveCurrentOpeningWord(categoryName: String) {
        mViewModel.saveWordToDb(categoryName)
    }

    private fun initView() {
        mViewModel.beginAnition.observe(viewLifecycleOwner, {
            if (it) {
                binding.progress.show()
            }
        })
        mViewModel.soundUsLoadState.observe(viewLifecycleOwner, {
            Handler(Looper.myLooper()!!).postDelayed({
                binding.progress.hide()
            }, 1000)

        })
        mViewModel.soundUkLoadState.observe(viewLifecycleOwner, {
            Handler(Looper.myLooper()!!).postDelayed({
                binding.progress.hide()
            }, 1000)
        })


        binding.reCyclerViewList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.reCyclerViewList.itemAnimator = null
        mDefinitionAdapter = DefinitionAdapter(requireContext())
        mDefinitionAdapter.callBack = {

            mViewModel.setUserDefinition(it)
        }
        binding.reCyclerViewList.adapter = mDefinitionAdapter


        mViewModel.mWordTypeSeparate.observe(viewLifecycleOwner, {
            mDefinitionAdapter.mSeparate = it
        })
        mViewModel.mDefinition.observe(viewLifecycleOwner, {
            Handler(Looper.myLooper()!!).postDelayed({
                binding.progress.hide()
            }, 2000)
            mDefinitionAdapter.setListWithAnimation(it.defList as ArrayList<Definition?>)
            binding.apply {
                txtPronUk.text = it.word.pronUk
                txtPronUs.text = it.word.pronUs
                getWordType(it.separate)
                txtDefinition1.text = it.word.word
            }


        })
        mViewModel.currentDisplayWordIsFavorite.observe(viewLifecycleOwner, {
            if (it) {
                binding.btnSave2.setImageResource(R.drawable.aic_favorite_small)
            } else {
                binding.btnSave2.setImageResource(R.drawable.aic_unfavorite_small)
            }
        })
        initListeners()
    }

    fun getWordType(list: List<WordTypeSeparate>) {
        var text = ""
        for (i in 0..list.size - 1) {
            text += list[i].wordType
            if (list.size - 1 - i > 0) {
                text += ", "
            }
        }
        binding.txtWordType.text = text
    }

//    var touching=false
//    var y=0
//    var preY=0
//    fun crossHideAnimation() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            binding.reCyclerViewList.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
////                Log.d("test", "crossHideAnimation: ${motionEvent.y}  ${motionEvent.rawX}")
//
//                if(motionEvent.action==MotionEvent.ACTION_UP){
//                    touching=false
//                    //user leave touch
//                }else if(motionEvent.action==MotionEvent.ACTION_MOVE){
//
//                    //user begin touch
//                }
//
////                motionEvent.rawX.log("rawX")
//                motionEvent.rawY.log("rawY")
//                motionEvent.action==MotionEvent.ACTION_MOVE.log("move")
//                if(motionEvent.action==MotionEvent.ACTION_DOWN){
//
//                }
//
//                return@OnTouchListener false
//            }
//
//            )
//            binding.reCyclerViewList.setOnScrollChangeListener { view, i, i2, i3, i4 ->
//
////                Log.d("test", "crossHideAnimation: ${view.x} : $i4")
//
//            }
//        }
//    }

//    fun onTouchEvent(event: MotionEvent): Boolean {
//        val y = event.y.toInt()
//        when (event.action) {
//            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {}
//        }
//        return false
//    }
//    fun xxxxxxx(){
//        val file = File(requireContext().filesDir.toString()+"/list.txt")
//        val lines= getFileContent(file.inputStream()).split(System.getProperty("line.separator"))
//
//        CoroutineScope(IO).launch {
//            for (i in 0..10){
//                delay(1000)
//                Log.d("test", "xxxxxxx: ${lines[i].lowercase().trim()}")
//                mViewModel.getWordFromServer(lines[i].lowercase().trim(), "", false)
//            }
//        }
//
//
//    }

    //    fun getFileContent(fis: FileInputStream): String {
//        BufferedReader(InputStreamReader(fis, "utf-8")).use { br ->
//            val sb = StringBuilder()
//            var line: String?
//            while (br.readLine().also { line = it } != null) {
//                sb.append(line)
//                sb.append('\n')
//            }
//            return sb.toString()
//        }
//    }
    private fun initListeners() {
        binding.speakerUK.setOnClickListener {
//            xxxxxxx()
            mViewModel.playSoundUk()
            if (mViewModel.soundUkLoadState.value == false) {
//                binding.progress.showNow()
            }
        }
        binding.speakerUS.setOnClickListener {
            mViewModel.playSoundUs()
            if (mViewModel.soundUsLoadState.value == false) {
//
            }

        }
        binding.btnSave2.setOnClickListener {
            val name = mViewModel.getWord()?.word
            mViewModel.getListCategoryName {
                if (it.size == 0) {
                    MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme).setTitle(
                            "No category found"
                        ).setMessage("Create now?")
                        .setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                            override fun onClick(p0: DialogInterface?, p1: Int) {
                                (requireActivity() as MainActivity2).binding.bottomNavigationView.selectTabAt(
                                    2,
                                    true
                                )
                                (requireActivity() as MainActivity2).showFavFragmentDisplayCreateCategoryDialog()
                            }

                        }).setNegativeButton("Cancel", /* listener = */ null).show();
                } else {
                    if (name == null) {
                        Toast.makeText(
                            requireContext(),
                            "Nothing here, try searching anything",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        AddToFavoriteDialog(it, name).show(childFragmentManager, "S")
                    }

                }

            }
        }

    }


    override fun saveCurrentWord() {
//        val name = mViewModel.getWord()?.word
//        mViewModel.mListCategoryName.observe(viewLifecycleOwner, {
//            if (dialog == null) {
//                dialog = (it)
//            } else if (!dialog!!.isShowing) {
////                dialog!!.word = name
//                dialog!!.listCategory = it
//            }
//            if ((requireActivity() as MainActivity2).currentTab == DEFINITION_TAB) {
//                dialog!!.show(childFragmentManager, "S")
//
//            }
//
//        })
    }

    override fun clickAtSearchItem(position: Int) {

    }


}

