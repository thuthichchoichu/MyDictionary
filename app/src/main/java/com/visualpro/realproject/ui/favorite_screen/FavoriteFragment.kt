package com.visualpro.realproject.ui.favorite_screen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.visualpro.realproject.MainApplication
import com.visualpro.realproject.adapter.AdapterInterfaces.onItemCategoryClick
import com.visualpro.realproject.adapter.CategoryAdapter
import com.visualpro.realproject.databinding.FavFragmentBinding
import com.visualpro.realproject.ui.DetailActivity
import com.visualpro.realproject.ui.MainActivity2.Companion.CATEGORY_COLOR
import com.visualpro.realproject.ui.MainActivity2.Companion.CATEGORY_DATA
import com.visualpro.realproject.ui.MainActivity2.Companion.CATEGORY_NAME
import com.visualpro.realproject.ui.MainActivity2.Companion.LAST_EDIT
import com.visualpro.realproject.ui.views_custom.AddCategoryDialog
import com.visualpro.realproject.viewmodel.FavoriteViewModel1


class FavoriteFragment : Fragment(), onItemCategoryClick {

    private lateinit var adapter: CategoryAdapter
    private val viewModel: FavoriteViewModel1 by viewModels {
        FavoriteViewModel1.FavoriteFragmentViewModelFactory((activity?.application as MainApplication).repository)
    }
    private var binding: FavFragmentBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        binding = FavFragmentBinding.inflate(layoutInflater, container, false)
        init()
        return binding!!.root
    }

    private fun init() {
        initAdapter()
        binding!!.rcvFavorites.getViewTreeObserver()
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding!!.rcvFavorites.getViewTreeObserver().removeGlobalOnLayoutListener(this)
                    adapter.parentHight = binding!!.mainLayout.height
                }
            })

        binding!!.fbtnCreate.setOnClickListener{
            val list=ArrayList<String>()
            val mList= adapter.mList
            if(mList!=null){
                for (i in mList.indices){
                    list.add(mList[i].category.categoryName)
                }
            }
            AddCategoryDialog(list)
                .show(childFragmentManager, "")}
        }

    @SuppressLint("NotifyDataSetChanged")
    private fun initAdapter() {
        adapter = CategoryAdapter(viewModel.getCategoryData(), requireContext(), this)

        viewModel.categoryData.observe(viewLifecycleOwner) {
            if (it.size == 0) {
                binding!!.rcvFavorites.visibility = View.GONE
                binding!!.layoutNoFavorite.visibility = View.VISIBLE
            } else {
                Log.d(" test", "update->: ${it.get(0).category.color}")
                binding!!.layoutNoFavorite.visibility = View.GONE
                binding!!.rcvFavorites.visibility = View.VISIBLE
                adapter.mList = it
                adapter.notifyDataSetChanged()
            }

        }

        binding!!.rcvFavorites.adapter = adapter
        binding!!.rcvFavorites.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }




    override fun onClick(position: Int) {
        val list=adapter.mList!![position]
        Intent(requireContext(), DetailActivity::class.java).apply {
            putExtra(CATEGORY_COLOR,list.category.color)
            putExtra(CATEGORY_NAME, list.category.categoryName)
            putParcelableArrayListExtra(CATEGORY_DATA,list.wordList.toCollection(ArrayList()))
            putExtra(LAST_EDIT, list.category.lastEdit)
            startActivity(this)
        }
    }

    override fun onLongClick(position: Int) {
    }



}