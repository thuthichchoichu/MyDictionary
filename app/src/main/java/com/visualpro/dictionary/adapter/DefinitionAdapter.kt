package com.visualpro.dictionary.adapter

import android.content.Context
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
import com.visualpro.dictionary.R
import com.visualpro.dictionary.model.Example
import com.visualpro.dictionary.model.WordTypeSeparate
import com.visualpro.dictionary.ui.views_custom.RecyclerViewAnimation
import com.visualpro.dictionary.views.views_custom.Animation

class DefinitionAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val TEXT = 0
    val DEFINITION = 1
    var callBack:((Definition)-> Unit)?=null
    var mList = ArrayList<Definition?>()

    var mSeparate: List<WordTypeSeparate> = ArrayList()

    var animated: Int = -1
    fun setListWithAnimation(list: ArrayList<Definition?>) {
        animated = -1
        mList = list
        notifyDataSetChanged()


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        if (viewType == TEXT) {
            return WordTypeGroupHolder(
                inflater.inflate(
                    R.layout.item_wordtype_seperate, parent, false
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
        val btnAdd: ImageView = itemView.findViewById(R.id.btn_Add_Example)
        val btnConfirm: ImageView = itemView.findViewById(R.id.btn_confirm)
        var editText: EditText = itemView.findViewById(R.id.edt_AddExample)
        var img_Expand: ImageView = itemView.findViewById(R.id.img_Expand)

    }

    class WordTypeGroupHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_WordType_Separate: TextView = itemView.findViewById(R.id.txt_WordType_Separate)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TEXT -> for (i in mSeparate.indices) {
                if (animated < position) {
                    Animation.animRow2(holder.itemView)
                }

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
                            it1.expand, holder.img_Expand, holder.layoutExpand
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
                        val userExam=Example(example, true)
                        mList[position]!!.example.apply {
                            add(userExam)
                        }
                        callBack!!.invoke(mList[position]!!)
                        notifyItemChanged(pos)
                    }

                    editText.clearFocus()
                    editText.setText("")
                    editText.hint = " ....."
                    holder.editText.isEnabled = false
                    holder.btnAdd.setVisibility(View.VISIBLE)
                    holder.btnConfirm.setVisibility(View.INVISIBLE)
                })

                holder.rcvExample.adapter = UserExample_Adapter(mList.get(position)?.example)

                holder.rcvExample.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                if (animated < position) {
                    animated = position
                    Animation.animRow(holder.itemView)

                }
//
            }

        }


    }
}



