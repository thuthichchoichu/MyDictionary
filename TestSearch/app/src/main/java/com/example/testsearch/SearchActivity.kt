package com.example.testsearch

import android.app.Activity
import android.app.ListActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import android.app.SearchManager

import android.content.Intent
import android.util.Log


class SearchActivity : Activity() {
    var myBaseAdapter: MyBaseAdapter? = null;
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.d("test","Ol");
        super.onCreate(savedInstanceState, persistentState)
        myBaseAdapter = MyBaseAdapter(this, arrayListOf())
//        listAdapter = myBaseAdapter;
        val intent = intent
        if (intent.hasExtra("searchTerm")) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            doMySearch(query)
        }
    }

    private fun doMySearch(query: String?) {
        var a: ArrayList<String> = ArrayList();
        a.add("Dsdsd")
        a.add("Dsdsd");
        myBaseAdapter!!.items = a
        myBaseAdapter!!.notifyDataSetChanged()

    }
}