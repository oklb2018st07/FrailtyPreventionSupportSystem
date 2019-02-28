package com.oklb2018.frailtypreventionsupportsystem

import android.content.Context
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.oklb2018.frailtypreventionsupportsystem.elements.FileManager
import com.oklb2018.frailtypreventionsupportsystem.fragments.*
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 以下の外部ライブラリを使用しています．
 * https://github.com/PhilJay/MPAndroidChart
 */
class MainActivity : AppCompatActivity(), OnReadyListener, OnReturnListener {

    private val titles = listOf(
        "フレイル\nチェック", "ウォーキング", "食事の記録", "頭の体操",
        "データ確認", "健康支援事業を探す", "トレイルメイキングテスト", "コイン取り\nゲーム", "単語作り\nゲーム",
        "共想法で\n話そう！"
    )

    /**
     * 下記より拝借
     * http://icooon-mono.com/
     */
    private val images = listOf(
        R.drawable.icon01, R.drawable.icon02, R.drawable.icon03,
        R.drawable.icon04, R.drawable.icon05, R.drawable.icon06,
        R.drawable.icon07, R.drawable.icon08, R.drawable.icon09,
        R.drawable.icon10
        )

    private val menus = List(titles.size) { i -> MenuListData(titles[i], images[i]) }

    private var isMenuListActive = true

    /**
     * 周知の通り，onCreate
     * このメソッドから処理が開始される（定義厨におこられそうな説明）
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val menuListAdapter = MenuListAdapter(this, menus)
        listView1.adapter = menuListAdapter

        listView1.setOnItemClickListener { parent, view, pos, id ->
            run {
                Log.d("debug", "pos: $pos, id: $id")
                for (v in parent.touchables) v.setBackgroundResource(R.drawable.colored_rectangler_00)
                view.setBackgroundResource(R.drawable.colored_rectangler_01)
                when (id) {
                    0L -> {
                        menuTranslateEnlargement()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, ChecklistFragment())
                        fragmentTransaction.commit()
                    }
                    1L -> {
                        menuTranslateEnlargement()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, WalkingFragment())
                        fragmentTransaction.commit()
                    }
                    2L -> {
                        menuTranslateEnlargement()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, MealsFragment())
                        fragmentTransaction.commit()
                    }
                    3L -> {
                        menuTranslateEnlargement()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, BrainTrainingOptionFragment())
                        fragmentTransaction.commit()
                    }
                    4L -> {
                        menuTranslateEnlargement()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, UserDataFragment())
                        fragmentTransaction.commit()
                    }
                    5L -> {
                        menuTranslateEnlargement()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, FindCommunityFragment())
                        fragmentTransaction.commit()
                    }
                    6L -> {
                        menuTranslateReduction()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, TMTFragment())
                        fragmentTransaction.commit()
                    }
                    7L -> {
                        menuTranslateReduction()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, GridGameFragment())
                        fragmentTransaction.commit()
                    }
                    8L -> {
                        menuTranslateReduction()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, GridWordGameFragment())
                        fragmentTransaction.commit()
                    }
                    9L -> {
                        menuTranslateReduction()
                        val fragmentTransaction = supportFragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.subContentArea, IdeaCommunicationFragment())
                        fragmentTransaction.commit()
                    }
                    else -> {
                    }
                }
            }
        }

        FileManager().checkExternalStoragePermission(this)
        FileManager().makeDirectory()
        FileManager().makeFiles()
        FileManager().initFiles()

        Log.d("debug", FileManager().CsvReader(fileName = FileManager.mealsResultFileName).read())

        WalkingFragment.readWalkingParameters()
        BrainTrainingFragment.readBrainTrainingParameters()
        FileManager().initWalkingParameter()
    }

    override fun onReady(skinId: Int, difficulty: Int) {
        menuTranslateReduction()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val brainTrainingFragment = BrainTrainingFragment()
        brainTrainingFragment.skinId = skinId
        brainTrainingFragment.difficulty = difficulty
        fragmentTransaction.replace(R.id.subContentArea, brainTrainingFragment)
        fragmentTransaction.commit()
    }

    override fun onReturn() {
        menuTranslateEnlargement()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.subContentArea, BrainTrainingOptionFragment())
        fragmentTransaction.commit()
    }

    private fun menuTranslateEnlargement() {
        if (isMenuListActive) return else isMenuListActive = true
        val param01 = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.width0),
            LinearLayout.LayoutParams.WRAP_CONTENT,
            5f
        )
        val param02 = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.width0),
            LinearLayout.LayoutParams.MATCH_PARENT,
            10f
        )
        space02.layoutParams = param01
        subContentArea.layoutParams = param02
    }

    private fun menuTranslateReduction() {
        if (!isMenuListActive) return else isMenuListActive = false
        val param01 = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.width60),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val param02 =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        space02.layoutParams = param01
        subContentArea.layoutParams = param02
    }
}

/**
 * MenuListAdapterの生成に対して必要なデータを保持するdata class
 */
data class MenuListData(val title: String, val imageId: Int)

/**
 * MenuListAdapterにおける，リソース保持用のdata class
 */
data class ViewHolder(val titleTextView: TextView, val menuImageView: ImageView)

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
            viewHolder = ViewHolder(view.findViewById(R.id.titleTextView), view.findViewById(R.id.menuImageView))
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        val listItem = getItem(position)
        viewHolder!!.titleTextView.text = listItem!!.title
        viewHolder!!.menuImageView.setImageBitmap(BitmapFactory.decodeResource(context.resources, listItem.imageId))
        return view!!
    }
}
