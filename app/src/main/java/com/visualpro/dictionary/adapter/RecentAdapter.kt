package com.visualpro.dictionary.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.visualpro.dictionary.R
import com.visualpro.dictionary.model.RecentItem

class RecentAdapter() : RecyclerView.Adapter<RecentAdapter.DHolder>() {
    var mList: List<RecentItem>?=null
    var onClick: ((Int)-> Unit)? = null
    inner class DHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        var text=itemView.findViewById<TextView>(R.id.txt_recentText)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(p0: View?) {
            onClick!!.invoke(adapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DHolder(inflater.inflate(R.layout.item_recent, parent,false))
    }

    override fun onBindViewHolder(holder: DHolder, position: Int) {
        holder.text.text=mList!![position].word
       if(mList!![position].isDefinition){


       }



    }

    override fun getItemCount(): Int {
       if( mList==null){
           return 0
       }
        return mList!!.size
    }
}