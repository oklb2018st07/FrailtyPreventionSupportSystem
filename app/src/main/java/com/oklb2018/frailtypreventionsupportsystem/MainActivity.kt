package com.oklb2018.frailtypreventionsupportsystem

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.oklb2018.frailtypreventionsupportsystem.elements.FileManager
import com.oklb2018.frailtypreventionsupportsystem.fragments.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val titles = listOf("Check List", "Walking", "Meals", "Brain Training", "My Data", "Find Activity")

    /**
     * 下記より拝借
     * http://icooon-mono.com/
     */
    private val images = listOf(
        R.drawable.icon01, R.drawable.icon02, R.drawable.icon03,
        R.drawable.icon04, R.drawable.icon05, R.drawable.icon06
    )

    private val menus = List(titles.size) { i -> MenuListData(titles[i], images[i]) }

    /**
     * 周知の通り，onCreate
     * このメソッドから処理が開始される（定義厨におこられそうな説明）
     */
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val menuListAdapter = MenuListAdapter(this, menus)
        listView1.adapter = menuListAdapter

        listView1.setOnItemClickListener { parent, view, pos, id ->
            run {
                for (v in parent.touchables) v.setBackgroundResource(R.drawable.colored_rectangler_00)
                parent.getChildAt(pos).setBackgroundResource(R.drawable.colored_rectangler_01)
                when (id) {
                    0L -> {
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, ChecklistFragment())
                        fragmentTransaction.commit()
                    }
                    1L -> {
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, WalkingFragment())
                        fragmentTransaction.commit()
                    }
                    2L -> {
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, MealsFragment())
                        fragmentTransaction.commit()
                    }
                    3L -> {
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, BrainTrainingFragment())
                        fragmentTransaction.commit()
                    }
                    4L -> {
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, UserDataFragment())
                        fragmentTransaction.commit()
                    }
                    5L -> {
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, FindCommunityFragment())
                        fragmentTransaction.commit()
                    }
                    else -> {
                    }
                }
            }
        }

        Log.d("a", "default path : ${Environment.getExternalStorageDirectory().path}")

        FileManager().checkExternalStoragePermission(this)
        FileManager().makeDirectory()
        FileManager().CsvWriter(fileName = "test.csv").write("name, id, num\nHiroshi, 810, 931")
    }
}

/**
 * MenuListAdapterの生成に対して必要なデータを保持するdata class
 */
data class MenuListData(val title: String, val imageId: Int)

/**
 * MenuListAdapterにおける，リソース保持用のdata class
 */
data class ViewHolder(val titleTextView: TextView, val imageView: ImageView)

/**
 * メニューとして表示されるCustom ListView用のアダプタ．
 * data class MenuListDataのListを受け取り，viewを生成する
 */
class MenuListAdapter(context: Context, menus: List<MenuListData>) : ArrayAdapter<MenuListData>(context, 0, menus) {
    private val layoutInflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var viewHolder: ViewHolder?
        var view = convertView
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_items_1, parent, false)
            viewHolder = ViewHolder(view.findViewById(R.id.titleTextView), view.findViewById(R.id.imageView))
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val listItem = getItem(position)
        viewHolder.titleTextView.text = listItem!!.title
        viewHolder.imageView.setImageBitmap(BitmapFactory.decodeResource(context.resources, listItem.imageId))

        return view!!
    }

}
