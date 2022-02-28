package com.visualpro.realproject.ui.translateScreen

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.visualpro.realproject.R
import com.visualpro.realproject.viewmodel.GGTranslateViewModel
import java.util.*


class FirstSelectLanguage : DialogFragment() {
    private lateinit var spinnerIn:AppCompatSpinner
    private lateinit var spinneOut:AppCompatSpinner
    private lateinit var viewModel:GGTranslateViewModel
    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel=ViewModelProvider(requireParentFragment()).get(GGTranslateViewModel::class.java)
    }

    private fun init(view:View) {
        val list = resources.getStringArray(R.array.countries_array)
        val list2=ArrayList<String>()
        list2.add(getString(R.string.auto_detect_recommend))
        list2.addAll(list)

        val listEntry = resources.getStringArray(R.array.countries_entry)
        val systemLanguage= Locale.getDefault().getLanguage()
        val adapterIn = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,list2.toArray())

        val adapterOut = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item,list)
        adapterOut.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        adapterIn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinneOut=view.findViewById(R.id.first_spinner_langout)
        spinneOut.adapter=adapterOut
        spinnerIn=view.findViewById(R.id.first_spinner_langin)
        spinnerIn.adapter=adapterIn

        for (i in listEntry.indices){
            if(systemLanguage.equals(listEntry[i])){
                spinneOut.setSelection(i)
                break
            }
        }
        spinnerIn.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2==0){
                    viewModel.setDetectInput(true)
                }else{
                    viewModel.setDetectInput(false)
                    viewModel.updateSrcIndexLanguageSelectedValue(p2-1)
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
        spinneOut.onItemSelectedListener= object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                viewModel.updateDesIndexLanguageSelectedValue(p2)
                }



            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.first_ask_language, null)
        builder.setView(view)
        init(view)

        builder.setPositiveButton("OK",{ dialogInterface, i ->
            viewModel.firstSetUpLanguageSuccessed()
        })

        return builder.create()

//        val builder =AlertDialog.Builder(requireContext())
//        val view: View =layoutInflater.inflate(R.layout.layout_add_category,null)
//        builder.setView(view)
//            .setTitle(null)
//            .setNegativeButton("Cancel",null)
//            .setPositiveButton("Add", { dialogInterface, i ->
//                run {
//                    val categoryName=view.findViewById<EditText>(R.id.edt_CreateCategory).text.toString().trim()
//                    create.createMe(categoryName)
//                }
//            })
//        return builder.create()
//    }
    }

    override fun onResume() {
        super.onResume()
        Log.d("test", "onResumes: ")
//        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
//        params.width = ViewGroup.LayoutParams.MATCH_PARENT
//        params.height =ViewGroup.LayoutParams.WRAP_CONTENT
//        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
    }
    fun pxFromDp(dp: Int): Float {
        return dp * getResources().getDisplayMetrics().density
    }

}