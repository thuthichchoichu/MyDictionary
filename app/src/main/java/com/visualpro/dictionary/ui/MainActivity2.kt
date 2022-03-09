package com.visualpro.dictionary.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.visualpro.dictionary.MainApplication
import com.visualpro.dictionary.R
import com.visualpro.dictionary._interface.CreateCategoryWithName
import com.visualpro.dictionary._interface.onRequestSaveCurrentWord
import com.visualpro.dictionary.adapter.BottomNavigationAdpater
import com.visualpro.dictionary.databinding.ActivityMain222Binding
import com.visualpro.dictionary.ui.favorite_screen.FavoriteFragment
import com.visualpro.dictionary.viewmodel.MainActivityViewModel
import com.visualpro.myapplication.Model.Category
import nl.joery.animatedbottombar.AnimatedBottomBar
import java.text.SimpleDateFormat
import java.util.*


class MainActivity2 : AppCompatActivity(), CreateCategoryWithName {

    companion object {
        const val CATEGORY_NAME = "categoryName"
        const val CATEGORY_COLOR = "category color"
        const val CATEGORY_DATA = "category data"
        const val LAST_EDIT = "lastedit"
        const val SOUND_TEMPORARY_US = "temporary.us"
        const val SOUND_TEMPORARY_UK = "temporary.uk"
        const val SOUND_TEMPORARY_DAILY_WORD = "dailyword.uk"
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
        installSplashScreen().apply {
            setKeepOnScreenCondition { mViewModel.stateSplashScreen.value }
        }
        setUpViewPager()
        setSupportActionBar(binding.toolbar)
        getSupportActionBar()!!.setDisplayShowTitleEnabled(false);
        setContentView(binding.root)

//        onNewIntent(intent)


        binding.fragmentContainer.setUserInputEnabled(false);
//        val lis=binding.bottomNavigationView.tabs[3]
//        binding.bottomNavigationView.removeTabAt(3)
//        CoroutineScope(Dispatchers.Main).launch{
//            delay(3000)
//            binding.bottomNavigationView.addTab(lis)
//        }
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

    fun showFavFragmentDisplayCreateCategoryDialog() {
        (findFragmentAtPosition(2) as FavoriteFragment).showCreateDialog()
    }

    fun findFragmentAtPosition(position: Int): Fragment? {
        return supportFragmentManager.findFragmentByTag("f$position")
    }

    private fun setUpViewPager() {
        binding.fragmentContainer.adapter =
            BottomNavigationAdpater(supportFragmentManager, lifecycle)
        binding.fragmentContainer.offscreenPageLimit = 4

    }

    override fun createMe(categoryName: String) {
        var category = Category().apply {
            this.categoryName = categoryName
            this.lastEdit = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date())

        }
        mViewModel.addCategory(category)
    }

    private fun findChildFragmentByViewPagerPosition(pos: Int): Fragment? {
        val tag = "android:switcher:${R.id.fragment_container}:$pos"
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
}

