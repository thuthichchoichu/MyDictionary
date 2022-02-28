package com.visualpro.dictionary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.visualpro.dictionary.R
import com.visualpro.dictionary.interfaces.AdapterSelectCallBack

class SelectLanguageAdapter(var context: Context, var indexItemSelected: Int, var callBack: AdapterSelectCallBack) : RecyclerView.Adapter<SelectLanguageAdapter.Holder>(), Filterable {
    var mList2: ArrayList<LanguageItem>? = null
        set(value) {
            if (field == null) {
                listTemporary.addAll(value!!)
            }
            field = value
        }
    private var inSearchMode = false
    var listTemporary = ArrayList<LanguageItem>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_select_language, parent, false)
        return Holder(view)

    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.txt.text = mList2!!.get(position).language
        holder.txt1.text = mList2!!.get(position).languageEntry

        if (mList2!![position].isSelected) {
            holder.txt1.setTextColor(context.resources.getColor(R.color.textColor))
            holder.txt.setTextColor(context.resources.getColor(R.color.textColor))
            holder.img.visibility = View.VISIBLE
        } else {
            holder.img.visibility = View.GONE
            holder.txt1.setTextColor(context.resources.getColor(R.color.black1))
            holder.txt.setTextColor(context.resources.getColor(R.color.black1))
        }
        holder.itemView.setOnClickListener(View.OnClickListener {
            if (inSearchMode) {
                val country = mList2!![position].languageEntry
                mList2!![position].isSelected = true
                if (indexItemSelected < mList2!!.size) {
                    mList2!![indexItemSelected].isSelected = false
                    notifyItemChanged(indexItemSelected)
                }

                listTemporary[indexItemSelected].isSelected = false
                for (i in listTemporary.indices) {
                    if (country.equals(listTemporary[i].languageEntry)) {
                        listTemporary[i].isSelected = true
                        indexItemSelected = i
                        break
                    }
                }


                callBack.languageSelect(indexItemSelected)
                notifyItemChanged(position)
            } else {
                listTemporary[indexItemSelected].isSelected = false
                listTemporary[position].isSelected = true
                mList2!![position].isSelected = true
                mList2!![indexItemSelected].isSelected = false
                notifyItemChanged(position)
                notifyItemChanged(indexItemSelected)
                indexItemSelected = position
                callBack.languageSelect(position)
            }
        })



    }

    override fun getItemCount() = mList2?.size ?: 0

    class Holder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var txt1: TextView = itemView.findViewById(R.id.language)
        var txt: TextView = itemView.findViewById(R.id.language1)
        var img: ImageView = itemView.findViewById(R.id.confirm_language)


    }

    data class LanguageItem(
        val language: String,
        val languageEntry: String,
        var isSelected: Boolean = false
    )


    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val filterList = ArrayList<LanguageItem>()
            if (p0 == null || p0.length == 0 || p0.equals("")) {
                inSearchMode = false
                filterList.addAll(listTemporary)
                callBack.scrollToPosition(indexItemSelected)
            } else {
                inSearchMode = true
                listTemporary.forEach {
                    if (it.language.startsWith(p0, true))
                        filterList.add(it)

                }
            }

            val results = FilterResults()
            results.values = filterList
            return results
        }


        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            mList2!!.clear()
            val list= p1?.values as ArrayList<LanguageItem>
             callBack.noResultFound(list.isEmpty())
            mList2!!.addAll(list)
            notifyDataSetChanged()
        }
    }
}