package com.visualpro.realproject.ui.views_custom

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.visualpro.realproject.R
import com.visualpro.realproject.ui.MainActivity2
import com.visualpro.realproject.ui.favorite_screen.FavoriteFragment

class AddCategoryDialog(val listCategoryName: ArrayList<String>) : DialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireParentFragment().requireActivity(), R.style.NormalDialogTheme)
        val view: View = layoutInflater.inflate(R.layout.create_dialog, null)
        val edittext: EditText = view.findViewById(R.id.edt_categoryname)
        var allow = true
        edittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               for(i in listCategoryName.indices){
                   if(listCategoryName[i].equals(p0.toString())){
                       edittext.error="Category $p0 already exists"
                       break
                   }
               }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        builder.setTitle("New category")
        builder.setNegativeButton("Cancel", null)
        builder.setPositiveButton("Ok", { i, i1 ->

            (requireParentFragment() as FavoriteFragment)
            (requireParentFragment().requireActivity() as MainActivity2).createMe(edittext.text.toString())
        })

        builder.setView(view)
        return builder.create()
    }


}