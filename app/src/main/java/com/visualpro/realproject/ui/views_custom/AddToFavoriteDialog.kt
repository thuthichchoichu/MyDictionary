package com.visualpro.realproject.ui.views_custom

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.visualpro.realproject.R
import com.visualpro.realproject.ui.DefinitionFragment


class AddToFavoriteDialog(var listCategory: List<String>, var name: String) : DialogFragment() {
    var isShowing = false
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(requireParentFragment().requireActivity(), R.style.NormalDialogTheme)
        val view: View =layoutInflater.inflate(R.layout.saveword,null)
        val spinner: Spinner =view.findViewById(R.id.spinnerSelectCategory)
        val adapter= ArrayAdapter(requireContext(),android.R.layout.simple_spinner_item, listCategory)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter=adapter
        builder.setTitle("Add $name to favorite")
        builder.setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    (requireParentFragment() as DefinitionFragment).saveCurrentOpeningWord(spinner.selectedItem.toString())
                    Toast.makeText(requireContext(), "Saved $name", Toast.LENGTH_LONG).show()
                }

            })
        builder.setNegativeButton("Cancel",  object : DialogInterface.OnClickListener{
            override fun onClick(p0: DialogInterface?, p1: Int) {
                dismiss()
            }

        })
        builder.setView(view)
        return builder.create()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        dismiss()
    }
}