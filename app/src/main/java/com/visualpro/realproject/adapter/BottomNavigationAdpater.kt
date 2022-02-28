package com.visualpro.realproject.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.visualpro.realproject.ui.DefinitionFragment
import com.visualpro.realproject.ui.dictionary_screen.DictionaryFragment
import com.visualpro.realproject.ui.favorite_screen.FavoriteFragment
import com.visualpro.realproject.ui.translateScreen.GG_TranslateFragment

class BottomNavigationAdpater(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount()=4

    override fun createFragment(position: Int): Fragment {
        when(position){
            0->return GG_TranslateFragment()
            1-> return DictionaryFragment()
            2->return FavoriteFragment()
            else ->return DefinitionFragment()
        }
    }
}