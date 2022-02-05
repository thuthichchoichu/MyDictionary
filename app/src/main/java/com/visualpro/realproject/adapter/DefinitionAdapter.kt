package com.visualpro.realproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.visualpro.myapplication.Model.Definition
import com.visualpro.realproject.Model.WordTypeSeparate
import com.visualpro.realproject.R
import com.visualpro.realproject.views.views_custom.RecyclerViewAnimation
import com.visualpro.realproject.views.views_custom.row_Slide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.system.measureTimeMillis

class DefinitionAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TEXT = 0
    val DEFINITION = 1
    var mCoroutine = CoroutineScope(Dispatchers.Main)
    var mList : List<Definition?> = ArrayList<Definition?>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
            animated=-1
        }
    var mSeparate : List<WordTypeSeparate> = ArrayList()

    var animated: Int = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == TEXT) {
            return WordTypeGroupHolder(
                inflater.inflate(
                    R.layout.item_wordtype_seperate,
                    parent,
                    false
                )
            )
        }
        return DefinitionHolder(inflater.inflate(R.layout.item_definition, parent, false))
    }

    private fun toggleLayout(isExpanded: Boolean, v: View, layoutExpand: RelativeLayout): Boolean {
        RecyclerViewAnimation.toggleArrow(v, isExpanded)
        if (isExpanded) {
            RecyclerViewAnimation.expand(layoutExpand)
        } else {
            RecyclerViewAnimation.collapse(layoutExpand)
        }
        return isExpanded
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        if (mList[position]!!.isNull) {
            return TEXT
        }
        return DEFINITION
    }


    class DefinitionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layoutExpand: RelativeLayout = itemView.findViewById(R.id.layout_expand)
        var txt_Def: TextView = itemView.findViewById(R.id.txt_Definition)
        var rcvExample: RecyclerView = itemView.findViewById(R.id.rcv_itemExample)
        val btnAdd: ImageView = itemView.findViewById(R.id.btn_add)
        val btnConfirm: ImageView = itemView.findViewById(R.id.btn_confirm)
        var editText: EditText = itemView.findViewById(R.id.edt_AddExample)
        var img_Expand: ImageView = itemView.findViewById(R.id.img_Expand)

    }

    class WordTypeGroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_WordType_Separate: TextView = itemView.findViewById(R.id.txt_WordType_Separate)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("test", "${position}: ${
            measureTimeMillis {
                when (getItemViewType(position)) {
                    TEXT -> for (i in mSeparate.indices) {
                        if (position < mSeparate.get(i).end) {
                            (holder as WordTypeGroupHolder).txt_WordType_Separate.text =
                                mSeparate[i].wordType
                            break
                        }
                    }
                    DEFINITION -> {
                        (holder as DefinitionHolder).itemView.setOnClickListener {
                            var show = mList.get(position)?.let { it1 ->
                                toggleLayout(
                                    it1.expand,
                                    holder.img_Expand,
                                    holder.layoutExpand
                                )
                            }
                            mList.get(position)?.reverseExpand()


                        }

//
                        holder.txt_Def.text = mList.get(position)?.definition!!
                        holder.btnAdd.setOnClickListener {
                            holder.editText.visibility = View.VISIBLE
                            holder.editText.setHint("Your example")
                            holder.editText.requestFocus()
                            holder.btnAdd.visibility = View.INVISIBLE
                            holder.btnConfirm.visibility = View.VISIBLE
                        }
                        holder.editText.onFocusChangeListener =
                            View.OnFocusChangeListener { v: View?, hasFocus: Boolean ->
                                if (!hasFocus) {

                                    holder.editText.clearFocus()
                                    holder.editText.setText("")
                                    holder.editText.hint = "..."
                                    holder.editText.isEnabled = false
                                    holder.btnAdd.setVisibility(View.VISIBLE)
                                    holder.btnConfirm.setVisibility(View.INVISIBLE)
                                }
                            }

                        holder.btnConfirm.setOnClickListener({
                            val editText = holder.editText
                            val pos = position
                            val example = editText.text.toString()
                            if (example != "") {
                                mList.get(position)?.userExample!!.add(example)
                                notifyItemChanged(pos)
                            }

                            editText.clearFocus()
                            editText.setText("")
                            editText.hint = " ....."
                            holder.editText.isEnabled = false
                            holder.btnAdd.setVisibility(View.VISIBLE)
                            holder.btnConfirm.setVisibility(View.INVISIBLE)
                        })

                        holder.rcvExample.adapter =
                            UserExample_Adapter(mList.get(position)?.oxfordExample)

                        holder.rcvExample.layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        if (animated < position) {
                            animated=position
                            row_Slide.animRow(holder.itemView, position)
                            
                        }
//
                    }

                }
            }
        } ms"
        )
    }
}



