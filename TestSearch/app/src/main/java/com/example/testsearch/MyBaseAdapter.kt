package com.example.testsearch

import android.content.ClipData
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.content.ClipData.Item
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView


class MyBaseAdapter : BaseAdapter {
    private var context: Context? = null
    public lateinit var items: ArrayList<String> ;

    constructor( context: Context, items:ArrayList<String>) : super(){
        this.context=context;
        this.items=items;

    }


    override fun getCount(): Int {
     return items.size
    }

    override fun getItem(p0: Int): Any {
        return items.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view: View?
        val vh: ListRowHolder
        if (p1 == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layoutitem, p2, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = p1
            vh = view.tag as ListRowHolder
        }

        vh.label.text = items.get(p0)
        Log.d("test", "getView: " + items.get(p0))
        return view!!
    }
    }
    private class ListRowHolder(row: View?) {
        val label: TextView

        init {
            this.label = row?.findViewById(R.id.txt) as TextView
        }
    }
