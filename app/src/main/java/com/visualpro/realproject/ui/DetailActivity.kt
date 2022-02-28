package com.visualpro.realproject.ui

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.visualpro.realproject.MainApplication
import com.visualpro.realproject.R
import com.visualpro.realproject.adapter.AdapterInterfaces.onItemCategoryClick
import com.visualpro.realproject.adapter.Category_DetailAdapter
import com.visualpro.realproject.databinding.ActivityDetailBinding
import com.visualpro.realproject.ui.MainActivity2.Companion.CATEGORY_COLOR
import com.visualpro.realproject.ui.MainActivity2.Companion.CATEGORY_DATA
import com.visualpro.realproject.ui.MainActivity2.Companion.CATEGORY_NAME
import com.visualpro.realproject.ui.MainActivity2.Companion.LAST_EDIT
import com.visualpro.realproject.viewmodel.DetailViewModel
import dev.sasikanth.colorsheet.ColorSheet


class DetailActivity : AppCompatActivity(), onItemCategoryClick {
    private lateinit var binding: ActivityDetailBinding
    private val mViewModel: DetailViewModel by viewModels {
        DetailViewModel.DetailActivityViewModelFactory((application as MainApplication).repository)
    }
    private lateinit var detailAdapter: Category_DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_mainActivityTheme)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        init()
        val intent = intent
        binding.viewModel = mViewModel
        if (intent != null) {
            mViewModel.categoryName = intent.getStringExtra(CATEGORY_NAME)!!
            mViewModel.mWordDetailsData.value = intent.getParcelableArrayListExtra(CATEGORY_DATA)
            mViewModel.categoryColor=intent.getIntExtra(CATEGORY_COLOR, -1)
            mViewModel.lastEdit=getString(R.string.lastedit,intent.getStringExtra(LAST_EDIT))



        }
        binding.mainLayout1.setBackgroundColor(mViewModel.categoryColor)
        window.setStatusBarColor( mViewModel.categoryColor)
        binding.txtLastEdit.setText(mViewModel.lastEdit)

    }

    fun init() {
        binding.btnPickColor.setOnClickListener {
            ColorSheet().colorPicker(colors = resources.getIntArray(R.array.color),
                noColorOption = true,
                selectedColor = mViewModel.categoryColor,
                listener = { color ->
                    Log.d("test", "color selected $color")
                    mViewModel.updateCategoryWithNewColor(color)
                    window.setStatusBarColor( color)
                    binding.mainLayout1.setBackgroundColor(color)
                }).show(supportFragmentManager)
        }


        mViewModel.mWordDetailsData.observe(this, {
            detailAdapter.mList = it
            detailAdapter.notifyDataSetChanged()
        })
        binding.txtCategory.setText(mViewModel.categoryName)
        binding.txtCategory.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                mViewModel.oldCategoryName=p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.txtCategory.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if(i==EditorInfo.IME_ACTION_DONE){
                if(!binding.txtCategory.text.toString().equals(mViewModel.categoryName)){
                    mViewModel.changeCategoryName(mViewModel.categoryName,binding.txtCategory.text.toString().trim() )
                }
                hideKeyboard(binding.txtCategory)
            }

            return@OnEditorActionListener true
        })

        detailAdapter = Category_DetailAdapter(this)
        binding.rcvMainShowCategory.adapter = detailAdapter
        binding.rcvMainShowCategory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.btnDeleteCategory.setOnClickListener({
            MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme).setTitle("Delete")
                .setMessage("All words will be lost")
                .setPositiveButton(android.R.string.ok, { _, _ ->
                    mViewModel.deleteThisCategory()
                    finish()
                }).setNegativeButton(android.R.string.no, null).show()


        })

    }
    fun hideKeyboard(view:View) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onClick(position: Int) {
//        val intent = Intent(context, ResultActivity::class.java)
//        intent.putExtra(Category_DetailAdapter.Holder.INTENT_ID,1)
//        intent.putExtra(RETRIEVE_FROM_CATEGORY, word[adapterPosition])
//        context.startActivity(intent)
//
//        val intent = Intent(this, ResultActivity::class.java)
//        intent.putExtra(INTENT_ID,1)
//        intent.putExtra(RETRIEVE_FROM_CATEGORY, detailAdapter.mList.get(position).word)
//        context.startActivity(intent)
    }

    override fun onLongClick(position: Int) {
        TODO("Not yet implemented")
    }


}