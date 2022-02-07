package com.visualpro.realproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.visualpro.myapplication.Model.Category
import com.visualpro.realproject.Model.model_relations.Category_WordList_Ref
import com.visualpro.realproject.R
import com.visualpro.realproject.adapter.AdapterInterfaces.onItemCategoryClick

class CategoryAdapter(var mList: List<Category_WordList_Ref>?, var context: Context, val onCategoryClick: onItemCategoryClick) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
    var parentHight: Int =0
    set(value) {
        field=value
        notifyDataSetChanged()
    }
    lateinit var colorDrawable: IntArray
//    lateinit var mList: List<Category_WordList_Ref>


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

        colorDrawable = IntArray(12)
        colorDrawable[0] = R.drawable.white
        colorDrawable[1] = R.drawable.red
        colorDrawable[2] = R.drawable.orange
        colorDrawable[3] = R.drawable.yellow
        colorDrawable[4] = R.drawable.green
        colorDrawable[5] = R.drawable.blue1
        colorDrawable[6] = R.drawable.lightblue
        colorDrawable[7] = R.drawable.darkblue
        colorDrawable[8] = R.drawable.violet
        colorDrawable[9] = R.drawable.pink
        colorDrawable[10] = R.drawable.brown
        colorDrawable[11] = R.drawable.gray

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

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {

            var params: ViewGroup.LayoutParams = holder.itemView.layoutParams
            when(mList!!.get(position).getSize()){
                Category.SMALLEST -> params.height=(parentHight*0.2).toInt()
                Category.MEDIUM -> params.height=(parentHight*0.35).toInt()
                Category.BIGGEST -> params.height=(parentHight*0.6).toInt()
            }
            holder.itemView.layoutParams=params





        holder.layout.background = AppCompatResources.getDrawable(context, colorDrawable[mList!!.get(position).category.color])
        holder.img_Pinned.visibility=if(mList!!.get(position).category.isPinned) View.VISIBLE else View.GONE
        holder.txt_CategoryName.text=mList!!.get(position).category.categoryName

        val wordListIsEmpty= mList!!.get(position).wordList.size==0
        if(!wordListIsEmpty){

            val list = mList!!.get(position).wordList
            var s=""
            for (i in list.indices){
                s+="${i}. ${list[i].word.word}. ${list[i].userDefinition[1].definition} \n"
            }
            holder.txt_WordList.text=s
        }else{
            holder.txt_WordList.text=""
        }



}

//    private fun getAllWord(wordList:List<UserWord_DefinitionList_Ref>) : String {
//        var s:String=""
//      for (i in wordList.indices){
//          s+= "${i} ."+ wordList.get(i).getWordDetails()+"\n\n"
//      }
//        return s
//    }
//
//    fun getWordDetails(defList:List<Definition>, word:String): String {
//        var example= "";
//        var j = 0;
//        for (i in 0 until (defList.size)) {
//            val tempList = defList.get(i)?.userExample
//            if (tempList != null) {
//                example = if (tempList.size > 0) example else ""
//                if (!example.equals("")) {
//                    j = i
//                    break
//                }
//            }
//        }
//
//        if(defList[j]!!.isNull){
//            j++
//        }
//        val textDisplay = word+ " " + " : " + defList.get(j)!!.definition
//
//
//        return if (example.equals("")) textDisplay else textDisplay + "\n" + "   - " + example;
//    }

    override fun getItemCount(): Int {
        return if(mList!=null) mList!!.size else 0
    }


}