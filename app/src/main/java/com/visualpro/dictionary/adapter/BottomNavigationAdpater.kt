package com.visualpro.dictionary.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.visualpro.dictionary.ui.definition_screen.DefinitionFragment
import com.visualpro.dictionary.ui.dictionary_screen.DictionaryFragment
import com.visualpro.dictionary.ui.favorite_screen.FavoriteFragment
import com.visualpro.dictionary.ui.translateScreen.TranslateFragment

class BottomNavigationAdpater(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount()=4

    override fun createFragment(position: Int): Fragment {
        when(position){
            0->return TranslateFragment()
            1-> return DictionaryFragment()
            2->return FavoriteFragment()
            else ->return DefinitionFragment()
        }
    }
}