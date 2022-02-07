package com.visualpro.realproject.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.View.*
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.visualpro.realproject.R
import com.visualpro.realproject.adapter.AdapterInterfaces.onItemCategoryClick
import com.visualpro.realproject.adapter.AdapterInterfaces.onItemRecyclerViewCkick
import com.visualpro.realproject.adapter.CategoryAdapter
import com.visualpro.realproject.adapter.SearchAdapter
import com.visualpro.realproject.databinding.ActivityMain222Binding
import com.visualpro.realproject.viewmodel.MainActivityViewModel
import com.visualpro.realproject.viewmodel.MainActivityViewModel.Companion.DATA_DEFINITION
import com.visualpro.realproject.views.ResultActivity.Companion.INTENT_ID
import com.visualpro.realproject.views.views_custom.AddCategoryDialog



class MainActivity2 : AppCompatActivity(), onItemRecyclerViewCkick, onItemCategoryClick {
    companion object{
        const val CATEGORY_NAME="category name"
        const val WORD_LIST="Word list"
        const val CATEGORY_ID="categoryId"
    }


    private lateinit var bind: ActivityMain222Binding
    val mViewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.MainActivityViewModelFactory((application as MainApplication).repository)
    }
    private lateinit var categoreAdapter: CategoryAdapter
    private lateinit var searchAdapter: SearchAdapter
    private var mHandler: Handler? = null
    private var time: Long = 0
    private lateinit var searchWidget: SearchView
    private var currentTab: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMain222Binding.inflate(layoutInflater)
        setSupportActionBar(bind.toolbar)
        setContentView(bind.root)
        init()
    }

    fun init() {
        initViewModels()
        initAdapter()
        mHandler = Handler(mainLooper)
        bind.btnAddCategory.setOnClickListener(OnClickListener {
            val diloag= AddCategoryDialog()
            diloag.show(supportFragmentManager,"")
        })
        bind.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
            currentTab=tab!!.position
                if (currentTab== 0) {
                    searchWidget.setQuery("", false)
                    searchWidget.queryHint = "Search a word"
                    bind.rcvMainSearchHint.visibility = VISIBLE
                    bind.relativeLayout3.visibility = GONE
                } else {
                    bind.rcvMainSearchHint.visibility = GONE
                    bind.relativeLayout3.visibility = VISIBLE
                    searchWidget.queryHint = "Enter text to translate"

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
        bind.rcvMain.getViewTreeObserver()
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    bind.rcvMain.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                    categoreAdapter.parentHight=bind.rcvMain.height
                }
    })
    }

    private fun initAdapter() {
        searchAdapter = SearchAdapter(mViewModel.mSearchItem_Instance().value, this)
        bind.rcvMainSearchHint.adapter = searchAdapter
        bind.rcvMainSearchHint.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bind.rcvMainSearchHint.setHasFixedSize(true)
        categoreAdapter= CategoryAdapter(mViewModel.getCategoryData(),this,this)

        mViewModel.mCategoryWordListLiveData.observe(this){

          categoreAdapter.mList=it
            categoreAdapter.notifyDataSetChanged()
        }

        bind.rcvMain.adapter=categoreAdapter
        bind.rcvMain.layoutManager=StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initViewModels() {


        mViewModel.mSearchItem_Instance().observe(this, {
            searchAdapter.mList = it
            Log.d("test", "time: ${System.currentTimeMillis() - time}")
            searchAdapter.notifyDataSetChanged()
            bind.progress.visibility= INVISIBLE
        })
        bind.viewModel=mViewModel
        mViewModel.mTranslateItem_Instance().observe(this, Observer { it ->
            bind.txtEnterTextToTranslate.setText(it.sentences.get(0).textOut)

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        searchWidget = menu!!.findItem(R.id.search_Widget).actionView as SearchView
        searchWidget.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if(currentTab==0){

                }else{
                    mViewModel.queryTranslate(query!!)
                    bind.progress.visibility= VISIBLE

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (currentTab==0) {
                    if (newText!!.length > 1) {
                        bind.progress.visibility = VISIBLE
                        mViewModel.querySeach(newText)
                        time = System.currentTimeMillis()
                    } else {
                        bind.progress.visibility = INVISIBLE

                    }
                } else {

                }
                return true
            }

        })
        searchWidget.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(p0: View?) {
                mHandler!!.postDelayed({
                    searchAdapter.leftMargin = searchWidget.left + searchWidget.paddingLeft
                }, 100)
                bind.linearLayout.visibility = VISIBLE
                bind.rcvMain.visibility = GONE
                bind.rcvMainSearchHint.visibility = VISIBLE
            }

            override fun onViewDetachedFromWindow(p0: View?) {
                bind.rcvMain.visibility = VISIBLE
                bind.rcvMainSearchHint.visibility = GONE
                bind.linearLayout.visibility = GONE
                bind.relativeLayout3.visibility = GONE
            }
        })
        return true
    }

    override fun clickAtSearchItem(position: Int) {
        var intent=Intent(this, ResultActivity::class.java)
        intent.putExtra(DATA_DEFINITION,searchAdapter.mList!!.get(position).word)
        intent.putExtra(INTENT_ID, 1)
        startActivity(intent)


    }

    override fun onClick(position: Int) {
//        val category = categoreAdapter.mList!!.get(position).wordList
//
//        val categoryName = categoreAdapter.mList!!.get(position).category.categoryName
//        val intent=Intent(this, DetailActivity::class.java )
//        intent.putExtra(CATEGORY_NAME, categoryName)
//        intent.putParcelableArrayListExtra(WORD_LIST, category as ArrayList<out Parcelable>)
        val intent=Intent(this, DetailActivity::class.java )
        intent.putExtra(CATEGORY_ID, position)
        startActivity(intent)

    }

    override fun onLongClick(position: Int) {
        TODO("Not yet implemented")
    }

}

