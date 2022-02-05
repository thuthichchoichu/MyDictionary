package com.visualpro.realproject.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.RelativeLayout.ALIGN_PARENT_RIGHT
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.visualpro.myapplication.Model.SearchItem_Local
import com.visualpro.realproject.R
import com.visualpro.realproject.adapter.AdapterInterfaces.onItemRecyclerViewCkick
import com.visualpro.realproject.adapter.SearchAdapter.SearchItemHolder

class SearchAdapter(var mList : ArrayList<SearchItem_Local>?, val onRcvItemCkick: onItemRecyclerViewCkick) : RecyclerView.Adapter<SearchItemHolder>() {
    var leftMargin : Int=0
    set(value) {
        if(value!=field)
            field=value+dpToPx(16)
            initViewsParams(field)
        notifyDataSetChanged()
    }

//     var mList: ArrayList<SearchItem_Local>? = null



    var paramsText: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
    );
    var paramsImgSearch: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT
    )
    var paramsImgPinText: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(
        RelativeLayout.LayoutParams.WRAP_CONTENT,
        RelativeLayout.LayoutParams.WRAP_CONTENT)


    private fun initViewsParams(leftMargin: Int) {
        var newMargin1 = (leftMargin - dpToPx(24))
        var newMargin = (leftMargin - dpToPx(24)) / 2


        paramsText.leftMargin = leftMargin
        paramsImgSearch.leftMargin = (newMargin1 * 0.65).toInt()
        paramsImgSearch.rightMargin = (newMargin1 * 0.35).toInt()
        paramsImgPinText.addRule(ALIGN_PARENT_RIGHT)
        paramsImgPinText.rightMargin += newMargin

    }

    private fun dpToPx(dp: Int): Int {
   return (dp* Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_search, parent, false)
        return SearchItemHolder(view, onRcvItemCkick)

    }

    override fun onBindViewHolder(holder: SearchItemHolder, position: Int) {
        holder.txt_SearchItem.text = mList!!.get(position).word
        if (mList!!.get(position).isHistory) {
            holder.img_SearchItem.setImageResource(R.drawable.ic_history_24)
        }

        holder.txt_SearchItem.layoutParams = paramsText
        holder.img_SearchItem.layoutParams = paramsImgSearch
        holder.img_Picktext.layoutParams = paramsImgPinText


    }

    override fun getItemCount(): Int {
        return if(mList==null) 0 else mList!!.size
    }

    class SearchItemHolder(itemView: View, var onRcvItemCkick: onItemRecyclerViewCkick) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        var txt_SearchItem: TextView = itemView.findViewById(R.id.txt_SearchText)
        var img_SearchItem: ImageView = itemView.findViewById(R.id.img_searchInSearchItem)
        var img_Picktext: ImageView = itemView.findViewById(R.id.img_pickText)

        override fun onClick(p0: View?) {
            onRcvItemCkick.clickAtSearchItem(adapterPosition)
        }


    }
}