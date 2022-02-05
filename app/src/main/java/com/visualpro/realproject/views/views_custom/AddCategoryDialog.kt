package com.visualpro.realproject.views.views_custom

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.visualpro.myapplication.Model.Category
import com.visualpro.realproject.R
import com.visualpro.realproject.views.MainActivity2

class AddCategoryDialog: DialogFragment() {
    private var mActivity:MainActivity2?=null
    override fun onAttach(context: Context) {
        if(context is MainActivity2){
            mActivity=context;
        }
        super.onAttach(context)
    }

    override fun onDetach() {
        mActivity=null
        super.onDetach()
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder =AlertDialog.Builder(mActivity)
        val view: View =layoutInflater.inflate(R.layout.layout_add_category,null)
        builder.setView(view)
            .setTitle(null)
            .setNegativeButton("Cancel",null)
            .setPositiveButton("Add", DialogInterface.OnClickListener { dialogInterface, i ->
                run {
                    val categoryName=view.findViewById<EditText>(R.id.edt_CreateCategory).text.toString().trim()
                    mActivity!!.mViewModel.addCategory(mActivity!!.applicationContext,Category(categoryName))
                }
            })
        return builder.create()
    }
}