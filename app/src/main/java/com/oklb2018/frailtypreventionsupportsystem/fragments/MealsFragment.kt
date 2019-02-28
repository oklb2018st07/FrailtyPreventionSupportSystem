package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.oklb2018.frailtypreventionsupportsystem.R
import com.oklb2018.frailtypreventionsupportsystem.elements.FileManager
import kotlinx.android.synthetic.main.meals_tutorial.*
import kotlinx.android.synthetic.main.sub_activity_main_meals.*
import kotlinx.android.synthetic.main.sub_activity_main_meals_result.*
import java.util.*
import kotlin.collections.ArrayList


class MealsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.meals_tutorial, container, false)
    }

    var answers = arrayListOf<Int>(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    var cnt = 0

    var images = listOf(
        R.drawable.food_niku_katamari,
        R.drawable.wasyoku_yakizakana,
        R.drawable.egg_ware_white,
        R.drawable.vegetable_daizu,
        R.drawable.milk_gyunyu_pack,
        R.drawable.flower_vegebouquet,
        R.drawable.kaisou_wakame,
        R.drawable.vegetable_ya_kon_yacon,
        R.drawable.fruits_basket,
        R.drawable.food_sald_oil
    )
    var questions = listOf(
        "普段，肉類をどの程度食べていますか？",
        "普段，魚介類をどの程度食べていますか？",
        "普段，卵料理をどの程度食べていますか？",
        "普段，大豆や大豆製品をどの程度食べていますか？",
        "普段，牛乳はどれくらい飲まれますか？",
        "普段，緑黄色野菜はどの程度食べていますか？",
        "普段，海藻類をどの程度食べていますか？",
        "普段，いも類をどの程度食べていますか？",
        "普段，果物をどの程度食べていますか？",
        "普段，油を使った料理をどの程度食べていますか？"
    )

    var foods = listOf("肉", "魚介類", "卵料理", "大豆・大豆製品", "牛乳", "緑黄色野菜", "海藻類", "いも類", "果物", "油")

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showTutorial()
    }

    private fun showTutorial() {
        mealsTutorialText.text = "これからあなたの食生活に関するいくつかの質問をさせていただきます．最近のあなたに当てはまる回答をタッチしてください．"
        mealsTutorialFinishButton.setOnClickListener { _ ->
            start()
        }
    }

    private fun start() {
        mealsTutorialFragmentParent.removeAllViews()
        layoutInflater.inflate(R.layout.sub_activity_main_meals, mealsTutorialFragmentParent)
        mealsQuestionTextView.text = questions[cnt]
        mealsImageView.setImageResource(images[cnt])
        mealsResponseButton00.setOnClickListener { respond(0) }
        mealsResponseButton01.setOnClickListener { respond(1) }
        mealsResponseButton02.setOnClickListener { respond(2) }
        mealsResponseButton03.setOnClickListener { respond(3) }
        mealsNextQuestionButton.setOnClickListener { questionUpdate(false) }
    }

    private fun respond(id: Int) {
        answers[cnt] = id
        if (cnt == questions.size - 1) {
            finish()
            return
        }
        questionUpdate(true)
    }

    private fun questionUpdate(next: Boolean) {
        if (next) cnt++ else cnt--
        if (cnt < 0) cnt = 0
        mealsQuestionTextView.text = questions[cnt]
        mealsImageView.setImageResource(images[cnt])
    }

    private fun finish() {
        FileManager().CsvWriter(fileName = FileManager.mealsResultFileName).writeMealsData(Calendar.getInstance(), answers)
        mealsFragmentParent.removeAllViews()
        layoutInflater.inflate(R.layout.sub_activity_main_meals_result, mealsFragmentParent)
        showMealsRadarChart()
    }

    private fun showMealsRadarChart() {
        val labels = ArrayList<String>()
        val entries01 = ArrayList<RadarEntry>()
        val entries02 = ArrayList<RadarEntry>()
        val history = getHistory()

        for (i in 0 until foods.size) {
            labels.add(foods[i])
            entries01.add(RadarEntry(answers[i].toFloat(), foods[i]))
            entries02.add(RadarEntry(history[i], foods[i]))
        }

        val radarDataSet01 = RadarDataSet(entries01, "今の栄養バランス").apply {
            color = Color.BLUE
            fillColor = Color.BLUE
            lineWidth = 3f
            setDrawFilled(true)
        }
        val radarDataSet02 = RadarDataSet(entries02, "過去の栄養バランス").apply {
            color = Color.RED
            fillColor = Color.RED
            lineWidth = 3f
            setDrawFilled(true)
        }
        mealsDataRadarChart01.data = RadarData(radarDataSet02, radarDataSet01).apply {
            setLabels(labels)
            setValueTextSize(10f)
        }
        mealsDataRadarChart01.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            textSize = 15f
            labelCount = 10
            setDrawLabels(true)
            setDrawGridLines(false)
            setDrawAxisLine(true)
        }
        mealsDataRadarChart01.yAxis.apply {
            axisMinimum = 0f
            axisMaximum = 3f
            setDrawLabels(false)
        }
        mealsDataRadarChart01.apply {
            isRotationEnabled = false
            description.isEnabled = false
            legend.isEnabled = false
        }
        mealsDataRadarChart01.invalidate()
    }

    private fun getHistory(): ArrayList<Float> {
        val data = arrayListOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)
        val lines = FileManager().CsvReader(fileName = FileManager.mealsResultFileName).readLines()
        for (i in lines.size - 5 until lines.size) {
            val row = lines[i].split(",")
            for (j in 0 until 10) {
                data[j] = data[j] + row[j + 1].toFloat()
            }
        }
        for (i in 0 until 10) {
            data[i] = data[i] / 5
        }
        Log.d("debug", "$data")
        return data
    }
}