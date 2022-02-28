package com.visualpro.realproject.ui

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.visualpro.realproject.R

class RequesGoogleAppDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view: View =layoutInflater.inflate(R.layout.download_google_dilog,null)
        builder.setView(view)
        val close=view.findViewById<ImageView>(R.id.btn_Close)
        close.setOnClickListener({
            dismiss()
        })
        val go=view.findViewById<Button>(R.id.got_It)
        go.setOnClickListener({

            dismiss()
        })
        return builder.create()
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent);
    }
    }
