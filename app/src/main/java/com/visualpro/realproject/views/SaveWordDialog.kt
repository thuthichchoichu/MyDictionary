package com.visualpro.realproject.views

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import com.visualpro.realproject.R


class SaveWordDialog(val word: String? = "", val list: List<String>, var save: dataSave) :
    DialogFragment() {

    private lateinit var spinner: Spinner
    private lateinit var progressBar:ProgressBar
    private lateinit var text:TextView
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = layoutInflater.inflate(R.layout.layout_save_word, null)
        spinner = view.findViewById(R.id.spinner_SelectCategory)
        progressBar=view.findViewById(R.id.progressBar)
        text=view.findViewById(R.id.txt2)

        val spinnerAdapter = ArrayAdapter(
            requireActivity().applicationContext,
            android.R.layout.simple_spinner_item,
            list
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.adapter = spinnerAdapter
        val builder = AlertDialog.Builder(requireContext())
            .setTitle("Select a category to save ${word}")
            .setPositiveButton("Ok",null)
            .create()


        builder.setView(view)
        return builder
    }

    override fun onResume() {
        super.onResume()
        val d = dialog as AlertDialog?
        if (d != null) {
            val positiveButton: Button = d.getButton(Dialog.BUTTON_POSITIVE) as Button
            positiveButton.setOnClickListener(View.OnClickListener {
                save.saveCurrentOpeningWord(spinner.selectedItemPosition)
                text.text="Saving.."
                text.setTextColor(Color.parseColor("#FF43A047"))
                progressBar.visibility=View.VISIBLE
                val handler=Handler(Looper.getMainLooper())
                handler.postDelayed(Runnable {
                   dismiss()

                },1000)
            })
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
       save.saveCurrentOpeningWord(-1)
    }
}


interface dataSave {
    fun saveCurrentOpeningWord(position: Int)
}