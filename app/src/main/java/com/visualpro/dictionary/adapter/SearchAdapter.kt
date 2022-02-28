package com.visualpro.dictionary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.visualpro.dictionary.R
import com.visualpro.dictionary.adapter.SearchAdapter.SearchItemHolder
import com.visualpro.dictionary.repositories.network.submodel.SearchText

class SearchAdapter : RecyclerView.Adapter<SearchItemHolder>() {

    var mList = ArrayList<SearchText>()
     var listenerClick : ((Int)-> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_search, parent, false)
        return SearchItemHolder(view)

    }

    override fun onBindViewHolder(holder: SearchItemHolder, position: Int) {
        holder.txt_SearchItem.text = mList.get(position).searchText
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class SearchItemHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        var txt_SearchItem: TextView = itemView.findViewById(R.id.txt_SearchText)
        override fun onClick(p0: View?) {
            listenerClick!!.invoke(adapterPosition)
        }


    }
}