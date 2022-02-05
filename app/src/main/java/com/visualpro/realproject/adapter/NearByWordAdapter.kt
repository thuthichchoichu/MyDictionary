package com.visualpro.realproject.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.visualpro.myapplication.Model.NearByWord
import com.visualpro.realproject.R
import com.visualpro.realproject.adapter.AdapterInterfaces.onItemNearByWordClick

class NearByWordAdapter(var onRcvClick: onItemNearByWordClick) : RecyclerView.Adapter<NearByWordAdapter.NBWHolder>() {
   var mList = ArrayList<NearByWord>()
    @SuppressLint("NotifyDataSetChanged")
    set(value) {
        field=value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NBWHolder {
       val inflater=LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_nearbyword,parent,false)
        return NBWHolder(view, onRcvClick)

    }

    override fun onBindViewHolder(holder: NBWHolder, position: Int) {
       holder.txt_NearByWord.text=mList.get(position).word
        holder.txt_WordType.text=mList.get(position).wordType
    }

    override fun getItemCount(): Int {
        return mList.size
    }
    class NBWHolder(itemView: View, var onRcvClick: onItemNearByWordClick) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        var txt_NearByWord: TextView = itemView.findViewById(R.id.txt_nearbyword)
        var txt_WordType: TextView = itemView.findViewById(R.id.txt_nearbyWordType)
        override fun onClick(p0: View?) {
            onRcvClick.nearByWordClick(adapterPosition)
        }
    }
}