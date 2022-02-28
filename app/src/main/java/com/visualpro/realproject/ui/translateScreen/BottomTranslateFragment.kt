package com.visualpro.realproject.ui.translateScreen

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayout
import com.visualpro.realproject.R
import com.visualpro.realproject.viewmodel.GGTranslateViewModel
import java.util.*


class BottomTranslateFragment(val text:String, val langIn:String, val langOut:String):BottomSheetDialogFragment() {

    private  var viewModel: GGTranslateViewModel?=null
    private lateinit var editText:EditText
    private lateinit var btn_Close: AppCompatImageView
    private lateinit var tab:TabLayout
    private lateinit var txt_TranslateFrom: TextView
    private lateinit var closeButon:ImageView

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel= ViewModelProvider(requireParentFragment()).get(GGTranslateViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root_layout = inflater.inflate(R.layout.bottom_translate,container,false)
        editText=root_layout.findViewById(R.id.editText)
        txt_TranslateFrom=root_layout.findViewById(R.id.translate_from)
        txt_TranslateFrom.setText("Translate from ${langIn.uppercase(Locale.ROOT)}")
        tab=root_layout.findViewById(R.id.tabLayout)
        closeButon=root_layout.findViewById(R.id.imageView3)
        closeButon.setOnClickListener({
            dismiss()
        })
        tab.getTabAt(0)?.text=langIn
        tab.getTabAt(1)?.text=langOut
        tab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    if(it.contentDescription?.equals(resources.getText(R.string.tab_in)) == true){
                        txt_TranslateFrom.setText("Translate from ${langIn.uppercase(Locale.ROOT)}")
                    }else{
                        txt_TranslateFrom.setText("Translate from${langOut.uppercase(Locale.ROOT)}")
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                tab?.let {
                    if(it.id==R.id.tabIn){
                        txt_TranslateFrom.setText("Translate from ${langIn}")
                    }else{
                        txt_TranslateFrom.setText("Translate from ${langOut}")
                    }
                }
            }

        })
        editText.setText(this.text)
        editText.setSelectAllOnFocus(true);
//        editText.setKeyImeChangeListener(this)
        editText.setRawInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
        editText.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if(i==EditorInfo.IME_ACTION_DONE){
             viewModel!!.translate(editText.text.toString().trim())
                (requireParentFragment() as GG_TranslateFragment).showProgressBar()
                dismiss()
            //                (parentFragment as TranslateFragment).
//                dismiss()
            }
            return@OnEditorActionListener true
        })
        btn_Close=root_layout.findViewById(R.id.imageView3)

        editText.setOnFocusChangeListener { view, b ->
            if(b){
                showKeyboard(view as EditText)
            }else{
                hideKeyboard()
            }
        }
        return  root_layout
    }
    fun showKeyboard(editText: EditText){
        val imm: InputMethodManager? = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }
    fun hideKeyboard() {
        val imm = requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun onResume() {
        super.onResume()
        editText.requestFocus()
    }

    override fun onStart() {
        super.onStart()
        view?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val behavior = BottomSheetBehavior.from(view!!.parent as View)
                behavior.peekHeight =resources.displayMetrics.heightPixels
            }
        })
    }

}