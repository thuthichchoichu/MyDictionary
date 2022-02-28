package com.visualpro.dictionary.ui.translateScreen

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.visualpro.dictionary.R
import com.visualpro.dictionary.adapter.SelectLanguageAdapter
import com.visualpro.dictionary.databinding.BottomSelectLanguageBinding
import com.visualpro.dictionary.interfaces.AdapterSelectCallBack
import com.visualpro.dictionary.ui.views_custom.RecyclerViewDisable
import com.visualpro.dictionary.viewmodel.GGTranslateViewModel


class BottomSelectLanguageFragment() : BottomSheetDialogFragment(), AdapterSelectCallBack {
    companion object {
        const val TYPE_TRANSLATE_FROM = 1
        const val TYPE_TRANSLATE_TO = 2
        const val TAG = "test"
    }

    var type = 0
        set(value) {
            field = value

        }
    private var binding: BottomSelectLanguageBinding? = null
    private var adapter: SelectLanguageAdapter? = null
    private var viewModel: GGTranslateViewModel? = null
    private val disableTouch = RecyclerViewDisable()
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Log.d(TAG, "onDismiss: ")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach: ")
        viewModel = ViewModelProvider(requireParentFragment()).get(GGTranslateViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        adapter = null
    }

    override fun onResume() {
        super.onResume()
        scrollToPosition(adapter!!.indexItemSelected)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BottomSelectLanguageBinding.inflate(inflater, container, false).apply {

            if (type == TYPE_TRANSLATE_TO) {
                textView8.visibility = View.GONE
                cardView.visibility = View.GONE
            } else {
                textView8.visibility = View.VISIBLE
                cardView.visibility = View.VISIBLE
            }

            edttt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0?.length == 0) {
                        text.text = resources.getString(R.string.all_language)
                    } else {
                        text.text = resources.getString(R.string.search_result)
                    }
                    adapter!!.filter.filter(p0)
                }

            })

            switchDetect.apply {
                viewModel!!.languageDetetectEnable.observe(viewLifecycleOwner, {
                    isChecked = it
                    if (it && type == TYPE_TRANSLATE_FROM) {
                        setFreezeSelectLanguge(it)
                    }
                })

                setOnCheckedChangeListener({ view, isChecked ->
                    val index =
                        if (type == TYPE_TRANSLATE_FROM) viewModel!!.indexInputLanguageSeleted.value else viewModel!!.indexOutputSelected.value
                    adapter!!.mList2!!.get(index!!).isSelected = !isChecked
                    adapter!!.notifyItemChanged(index)
                    if (type == TYPE_TRANSLATE_FROM) {
                        setFreezeSelectLanguge(isChecked)
                    }
                    viewModel!!.setDetectInput(isChecked)

                })
            }
            textView6.setOnClickListener {
                dismiss()
            }
            val mListContry = resources.getStringArray(R.array.countries_array)
            val mListContryentry = resources.getStringArray(R.array.countries_entry)
            val index =
                if (type == TYPE_TRANSLATE_FROM) viewModel!!.indexInputLanguageSeleted.value else viewModel!!.indexOutputSelected.value
            var mList2 = ArrayList<SelectLanguageAdapter.LanguageItem>()
            for (i in mListContry.indices) {
                mList2.add(
                    SelectLanguageAdapter.LanguageItem(
                        mListContry[i],
                        mListContryentry[i],
                        if (i == index && viewModel!!.languageDetetectEnable.value == false) true else false

                    )
                )
            }
            if (type == TYPE_TRANSLATE_FROM) {
                adapter = SelectLanguageAdapter(
                    requireContext(), index!!, this@BottomSelectLanguageFragment
                )
            } else {
                adapter = SelectLanguageAdapter(
                    requireContext(),
                    viewModel!!.indexOutputSelected.value!!,
                    this@BottomSelectLanguageFragment
                )

            }
            rcvSelectLanguages.adapter = adapter
            rcvSelectLanguages.layoutManager = LinearLayoutManager(requireContext())
            adapter!!.mList2 = mList2
            adapter!!.notifyDataSetChanged()

        }


        return binding!!.root
    }

    override fun onStart() {
        view?.viewTreeObserver?.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val behavior = BottomSheetBehavior.from(view!!.parent as View)
                behavior.peekHeight = resources.displayMetrics.heightPixels
            }
        })
        super.onStart()
    }

    override fun languageSelect(position: Int) {
        if (type == TYPE_TRANSLATE_FROM) {
            viewModel!!.updateSrcIndexLanguageSelectedValue(position)
        } else viewModel!!.updateDesIndexLanguageSelectedValue(position)
    }

    override fun noResultFound(noResultFound: Boolean) {
        binding!!.nolanguageFound.visibility = if (noResultFound) View.VISIBLE else View.GONE
    }

    override fun scrollToPosition(position: Int) {
        binding!!.rcvSelectLanguages.apply {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    scrollToPosition(position)
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }


    }

    fun setFreezeSelectLanguge(freeze: Boolean) {
        binding!!.apply {
            if (freeze) {
                edttt.isEnabled = false
                rcvSelectLanguages.addOnItemTouchListener(disableTouch)
                card1.setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.gray)
                )
            } else {
                edttt.isEnabled = true
                rcvSelectLanguages.removeOnItemTouchListener(disableTouch)
                card1.setCardBackgroundColor(
                    ContextCompat.getColor(requireContext(), R.color.white)
                )

            }


        }

    }
}