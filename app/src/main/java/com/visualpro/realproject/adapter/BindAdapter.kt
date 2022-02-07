package com.visualpro.realproject.adapter

import android.annotation.SuppressLint
import android.util.Log
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.visualpro.myapplication.Model.NearByWord
import com.visualpro.realproject.Model.WordTypeSeparate
import com.visualpro.realproject.Model.model_relations.Category_WordList_Ref
import com.visualpro.realproject.Model.model_relations.UserWord_DefinitionList_Ref
import com.visualpro.realproject.Model.model_relations.Word_DefinitionList_Ref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class BindAdapter {
    companion object {
        var mCoroutine = CoroutineScope(Dispatchers.Main)

        @JvmStatic
        @SuppressLint("NotifyDataSetChanged")
        @BindingAdapter("definitionData")
        fun setDataForRecyclerview(rcv: RecyclerView, data: Word_DefinitionList_Ref?) {
            if (data == null) {
                return
            }
            (rcv.adapter as DefinitionAdapter).mList = data.defList


        }


        @JvmStatic
        @BindingAdapter("dataSeparate")
        fun setSeperate(rcv: RecyclerView, data:List<WordTypeSeparate>?) {
            if (data == null) {
                return
            }
            (rcv.adapter as DefinitionAdapter).mSeparate = data
        }

        @JvmStatic
        @SuppressLint("NotifyDataSetChanged")
        @BindingAdapter("nearbyword_data")
        fun setDataNearByWord(rcv: RecyclerView, data: ArrayList<NearByWord>?) {
            if (data == null || rcv.adapter == null) {
                return
            }
            (rcv.adapter as NearByWordAdapter).mList = data


        }

        @JvmStatic
        @BindingAdapter("categoryData")
        fun setDataCategory(rcv: RecyclerView, data: List<Category_WordList_Ref>?) {
            if (data == null) {
                return
            }
            Log.d("test", "setDataCategory: ")
            (rcv.adapter as CategoryAdapter).mList = data
        }



        @JvmStatic
        @BindingAdapter("detailsData")
        fun setDetailsData(rcv: RecyclerView, data: List<UserWord_DefinitionList_Ref>?) {
            if (data == null) {
                return
            }
            (rcv.adapter as Category_DetailAdapter).mList = data
        }
    }
}