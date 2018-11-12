package com.oklb2018.frailtypreventionsupportsystem

import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var x: String = "hello world"
        text_view1.setText(x)

        var p: Point = Point()
        this.windowManager.defaultDisplay.getRealSize(p)
        Log.v("DisplaySize", "x = ${p.x}, y = ${p.y}")
        text_view1.text = "x = ${p.x}, y = ${p.y}"

        val texts = arrayOf("my", "name", " is", "shum", "desu")
        val adapter = ArrayAdapter(this, R.layout.list_items_2, texts)
        listView1.setAdapter(adapter)

        listView1.setOnItemClickListener { adapterView, view, position, id -> Log.v("tags", "onClick!") }
    }

}
