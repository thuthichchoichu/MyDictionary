package com.visualpro.realproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.visualpro.realproject.Model.model_relations.Word_DefinitionList_Ref
import com.visualpro.realproject.R
import com.visualpro.realproject.adapter.AdapterInterfaces.onItemCategoryClick
import java.util.*


class Category_DetailAdapter(private val context: Context, var mList: ArrayList<Word_DefinitionList_Ref>?, val onClick:onItemCategoryClick) : RecyclerView.Adapter<Category_DetailAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_word_detail_in_category_detail, parent, false)
        return Holder( view, onClick)
    }



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.txt_Category_Detail.setText("${position} ." + mList!!.get(position).getWordDetails()+"...\n")
    }

    override fun getItemCount(): Int {
        return if (mList == null) 0 else mList!!.size
    }

    class Holder(itemView: View , var onClick:onItemCategoryClick) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var txt_Category_Detail: TextView

        companion object {
            const val RETRIEVE_FROM_CATEGORY = "retrieve from category"

            //        public static final String WORD = "word";
            //        public static final String WORD_PRONUNCIATION = "pronunciation";
            //        public static final String WORD_TYPE = "word type";
            const val INTENT_ID = "intent id"
        }

        init {
            itemView.setOnClickListener(this)
            txt_Category_Detail = itemView.findViewById(R.id.txt_WordDetail)
        }

        override fun onClick(p0: View?) {
            onClick.onClick(adapterPosition)
        }
    }
}