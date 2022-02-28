package com.visualpro.realproject.ui.views_custom

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
            dismiss()
        })
        return builder.create()
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent);
    }
    }
