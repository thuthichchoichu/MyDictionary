package com.visualpro.dictionary.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.visualpro.dictionary.R
import com.visualpro.dictionary.model.Example

class UserExample_Adapter(var mList: ArrayList<Example>?) : RecyclerView.Adapter<UserExample_Adapter.HolderPro>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderPro {
        val inflater = LayoutInflater.from(parent.context)
        val contactView: View = inflater.inflate(R.layout.itemrow_user_example, parent, false)
        return HolderPro(contactView)
    }

    override fun onBindViewHolder(holder: HolderPro, position: Int) {
        holder.txt_ExampleItem.setText(mList!![position].example)
        if(mList!!.get(position).isUserExample){
            holder.txt_ExampleItem.setTextColor(Color.parseColor("#FF43A047"))
        }
//        holder.img_del.setOnClickListener {
//            val pos = holder.adapterPosition
//            if (isTxtExampleChanged) {
//                isTxtExampleChanged = false
//                mList!![pos] =holder.txt_ExampleItem.text.toString()
//                holder.img_del.setImageResource(R.drawable.ic_delete_exit_svgrepo_com)
//                notifyItemChanged(pos)
//            } else {
//                holder.img_del.setImageResource(0)
//                mList!!.removeAt(pos)
//                notifyItemRemoved(pos)
//            }
//        }
    }




    override fun getItemCount(): Int {
        return if(mList==null) 0 else mList!!.size
    }

    class HolderPro(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_ExampleItem: TextView

        init {
            txt_ExampleItem = itemView.findViewById(R.id.txt_ExampleItem)
        }
    }


}