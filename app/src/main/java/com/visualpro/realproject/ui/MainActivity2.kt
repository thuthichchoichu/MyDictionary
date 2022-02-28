package com.visualpro.realproject.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.visualpro.myapplication.Model.Category
import com.visualpro.realproject.MainApplication
import com.visualpro.realproject.R
import com.visualpro.realproject.TimerWatch
import com.visualpro.realproject.adapter.BottomNavigationAdpater
import com.visualpro.realproject.databinding.ActivityMain222Binding
import com.visualpro.realproject.interfaces.CreateCategoryWithName
import com.visualpro.realproject.interfaces.onRequestSaveCurrentWord
import com.visualpro.realproject.viewmodel.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.joery.animatedbottombar.AnimatedBottomBar


class MainActivity2 : AppCompatActivity(), CreateCategoryWithName {

    companion object {
        const val CATEGORY_NAME = "categoryName"
        const val CATEGORY_COLOR = "category color"
        const val CATEGORY_DATA = "category data"
        const val LAST_EDIT = "lastedit"
        const val SOUND_TEMPORARY_US = "temporary.us"
        const val SOUND_TEMPORARY_UK = "temporary.uk"
    }

    var callBackSaveCurrentWord: onRequestSaveCurrentWord? = null
    lateinit var binding: ActivityMain222Binding
    val mViewModel: MainActivityViewModel by viewModels {
        MainActivityViewModel.MainActivityViewModelFactory(
            (application as MainApplication).repository,
            (application as MainApplication).filesDir.toString()
        )
    }

    fun setNavigationMenuIcon(resID: Int?) {
        if (resID == null) {
            binding.toolbar.setNavigationIcon(null)
        } else {
            binding.toolbar.setNavigationIcon(resID)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_mainActivityTheme)
        super.onCreate(savedInstanceState)
        binding = ActivityMain222Binding.inflate(layoutInflater)
        setUpViewPager()
        setSupportActionBar(binding.toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        setContentView(binding.root)

//        onNewIntent(intent)


        binding.fragmentContainer.setUserInputEnabled(false);
        val lis=binding.bottomNavigationView.tabs[3]
        binding.bottomNavigationView.removeTabAt(3)
        CoroutineScope(Dispatchers.Main).launch{
            delay(3000)
            binding.bottomNavigationView.addTab(lis)
        }
        binding.bottomNavigationView.setOnTabSelectListener(object :
            AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                binding.fragmentContainer.setCurrentItem(newIndex, false)
                if (lastIndex == 1) {
                    setNavigationMenuIcon(null)
                }

            }

            override fun onTabReselected(index: Int, tab: AnimatedBottomBar.Tab) {
            }
        })
    }

//    override fun onNewIntent(intent: Intent?) {
//        super.onNewIntent(intent)
//        intent?.let {
//            val data = it.getStringExtra(FloatTranslate.NAVIGATION_TO_TRANSLATE_SCREEN)
//            if (data != null) {
//                Handler(mainLooper).postDelayed({
//                    (findChildFragmentByViewPagerPosition(0) as GG_TranslateFragment)
//                    binding.bottomNavigationView.selectTabAt(0, false)
//                }, 500)
//            }
//        }
//    }

    private fun setUpViewPager() {
        binding.fragmentContainer.adapter =
            BottomNavigationAdpater(supportFragmentManager, lifecycle)
        binding.fragmentContainer.offscreenPageLimit = 4
    }

    override fun createMe(categoryName: String) {
        var category = Category().apply {
            this.categoryName = categoryName
            this.lastEdit = TimerWatch.getTime()

        }
        mViewModel.addCategory(category)
    }

    private fun findChildFragmentByViewPagerPosition(pos: Int): Fragment? {
        val tag = "android:switcher:${R.id.fragment_container}:$pos"
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
}

