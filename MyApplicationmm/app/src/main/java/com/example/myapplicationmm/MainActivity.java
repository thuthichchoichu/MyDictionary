package com.example.myapplicationmm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private ArrayList<String> d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayList<String> ss = new ArrayList<>();
        foo(ss);

    }
    private void foo(ArrayList<String> d1){
        this.d=d1;
        Log.d("before",String.valueOf(this.d==d1) );
        d1= new ArrayList<>();
        Log.d("before",String.valueOf(this.d==d1) );
    }
}