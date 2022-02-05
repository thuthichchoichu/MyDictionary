package com.visualpro.realproject.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.visualpro.realproject.Model.model_relations.Word_DefinitionList_Ref
import com.visualpro.realproject.R
import com.visualpro.realproject.adapter.AdapterInterfaces.onItemCategoryClick
import com.visualpro.realproject.adapter.Category_DetailAdapter
import com.visualpro.realproject.databinding.ActivityDetailBinding
import com.visualpro.realproject.viewmodel.DetailViewModel
import com.visualpro.realproject.views.MainActivity2.Companion.CATEGORY_NAME
import com.visualpro.realproject.views.MainActivity2.Companion.WORD_LIST

class DetailActivity : AppCompatActivity(), onItemCategoryClick {
    private lateinit var binding: ActivityDetailBinding
    private val mViewModel: DetailViewModel by viewModels {
        DetailViewModel.DetailActivityViewModelFactory((application as MainApplication).repository)
    }
    private lateinit var detailAdapter:Category_DetailAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        init()
        val intent = intent
        if (intent != null) {
            val categoryName = intent.getStringExtra(CATEGORY_NAME)
            val categoryList = intent.getParcelableArrayListExtra<Word_DefinitionList_Ref>(WORD_LIST)
            mViewModel.categoryNameLiveData.value = categoryName
            mViewModel.categoryMutableLiveData.value = categoryList
        }

        binding.viewModel = mViewModel


    }
    fun init(){
        detailAdapter= Category_DetailAdapter(this, null,this )
        binding.rcvMainShowCategory.adapter=detailAdapter
        binding.rcvMainShowCategory.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)


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