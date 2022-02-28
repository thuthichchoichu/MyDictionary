package com.visualpro.dictionary.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.visualpro.myapplication.Model.Category
import com.visualpro.dictionary.R
import com.visualpro.dictionary.adapter.AdapterInterfaces.onItemCategoryClick
import com.visualpro.dictionary.model.model_relations.Category_WordList_Ref
import kotlin.system.measureTimeMillis

class CategoryAdapter(var mList: List<Category_WordList_Ref>?, var context: Context, val onCategoryClick: onItemCategoryClick) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
    var parentHight: Int =0
    set(value) {
        if(value ==  field ){
            return
        }
        field=value
        notifyDataSetChanged()
    }
    lateinit var colorDrawable: IntArray
    lateinit var compareList:IntArray
    init {
        initColorDrawable()
    }
   private fun getSize(wordListSize:Int): String {
        when (wordListSize) {
            0, 1 -> return Category.SMALLEST
            2, 3 -> return Category.MEDIUM;
            else -> {
                return Category.BIGGEST
            }
        }
    }

    private fun initColorDrawable() {
        Log.d("test", "initColorDrawable:${measureTimeMillis {
            colorDrawable = IntArray(12)
            colorDrawable[0] = R.drawable.white
            colorDrawable[1] = R.drawable.red
            colorDrawable[2] = R.drawable.orange
            colorDrawable[5] = R.drawable.blue1
            colorDrawable[3] = R.drawable.yellow
            colorDrawable[4] = R.drawable.green
            colorDrawable[7] = R.drawable.darkblue
            colorDrawable[6] = R.drawable.lightblue
            colorDrawable[8] = R.drawable.violet
            colorDrawable[9] = R.drawable.pink
            colorDrawable[10] = R.drawable.brown
            colorDrawable[11] = R.drawable.gray
            
            compareList=context.resources.getIntArray(R.array.color)
        }} ")


    }

    class CategoryHolder( itemView: View, var onCategoryClick: onItemCategoryClick) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener, View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
        }
        var layout:RelativeLayout=itemView.findViewById(R.id.background_mainRCV)
        var txt_CategoryName: TextView = itemView.findViewById(R.id.txt_CategoryName_RV)
        var txt_WordList:TextView=itemView.findViewById(R.id.txt_wordsList_RV)
        var img_Pinned: ImageView = itemView.findViewById(R.id.img_pinned)
        override fun onClick(p0: View?) {
            onCategoryClick.onClick(adapterPosition)
        }

        override fun onLongClick(p0: View?): Boolean {
            onCategoryClick.onLongClick(adapterPosition)
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CategoryHolder(
            inflater.inflate(R.layout.item_main_category_view, parent, false),
            onCategoryClick
        )
    }

    fun getDrawable(color:Int):Int{
        for (i in compareList.indices){
            if(compareList[i]==color){
                return i+1
            }
        }
        return 0
    }
    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
            var params: ViewGroup.LayoutParams = holder.itemView.layoutParams
            when(mList!!.get(position).getSize()){
                Category.SMALLEST -> params.height=(parentHight*0.2).toInt()
                Category.MEDIUM -> params.height=(parentHight*0.35).toInt()
                Category.BIGGEST -> params.height=(parentHight*0.5).toInt()
            }
            holder.itemView.layoutParams=params

            if(mList!![position].category.color==-1){
            holder.layout.background= AppCompatResources.getDrawable(context, colorDrawable[0])
        }else{
            holder.layout.background = AppCompatResources.getDrawable(context, colorDrawable[getDrawable(mList!![position].category.color)])
        }

        holder.img_Pinned.visibility=if(mList!!.get(position).category.isPinned) View.VISIBLE else View.GONE
        holder.txt_CategoryName.text=mList!!.get(position).category.categoryName

            val list = mList!!.get(position).wordList
            var s=""
            for (i in list.indices){
                s+="${i}. ${list[i].word.word}. ${list[i].userDefinition[1].definition} \n\n"
            }
            holder.txt_WordList.text=s





}


    override fun getItemCount(): Int {
        return if(mList!=null) mList!!.size else 0
    }


}