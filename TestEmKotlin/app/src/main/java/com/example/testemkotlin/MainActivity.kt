package com.example.testemkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.example.testemkotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var bind: ActivityMainBinding
    var mViewModel: MyViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)
        bind.tvCount.text = mViewModel!!.count++.toString();
        bind.btnDown.setOnClickListener(View.OnClickListener {
            mViewModel!!.count -= 1;
            bind.tvCount.text = mViewModel!!.count++.toString();
        })
        bind.btnUp.setOnClickListener(View.OnClickListener {
            mViewModel!!.count += 1;
            bind.tvCount.text = mViewModel!!.count.toString()
        })
    }

}