package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.oklb2018.frailtypreventionsupportsystem.R
import kotlinx.android.synthetic.main.sub_activity_main_user_data.*
import kotlinx.android.synthetic.main.sub_fragment_user_data_brain_training.*
import kotlinx.android.synthetic.main.sub_fragment_user_data_walking.*
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.oklb2018.frailtypreventionsupportsystem.elements.FileManager
import kotlinx.android.synthetic.main.sub_fragment_user_data_extra.*


class UserDataFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_activity_main_user_data, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.userDataArea, WalkingDataChartFragment())
        fragmentTransaction.commit()

        walkingDataShowButton.setOnClickListener {
            //showWalkingDataBarChart()
            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.userDataArea, WalkingDataChartFragment())
            fragmentTransaction.commit()
        }
        brainTrainingDataShowButton.setOnClickListener {
            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.userDataArea, BrainTrainingDataChartFragment())
            fragmentTransaction.commit()
        }
        extraDataShowButton.setOnClickListener {
            val fragmentTransaction = activity!!.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.userDataArea, ExtraDataFragment())
            fragmentTransaction.commit()
        }
    }

    private fun showExtraData() {

    }
}

class WalkingDataChartFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_fragment_user_data_walking, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //showWalkingDataBarChart()
        showWalkingDataBarChart01()
    }

    private fun showWalkingDataBarChart01() {
        val labels = ArrayList<String>()
        val entries01 = ArrayList<BarEntry>()
        val entries02 = ArrayList<BarEntry>()

        labels.add("")
        for (wp in WalkingFragment.walkingParameters) {
            labels.add(DateFormat.format("MM/dd", wp.calendar).toString())
        }
        for (i in 0 until WalkingFragment.walkingParameters.size) {
            entries01.add(BarEntry((i - 0.2).toFloat(), WalkingFragment.walkingParameters[i].goal.toFloat()))
            entries02.add(BarEntry((i + 0.2).toFloat(), WalkingFragment.walkingParameters[i].steps.toFloat()))
        }
        val barDataSet01 = BarDataSet(entries01, "目標歩数").apply {
            color = Color.RED
            barBorderWidth = 1f
        }
        val barDataSet02 = BarDataSet(entries02, "達成歩数").apply {
            color = Color.BLUE
            barBorderWidth = 1f
        }
        walkingDataChart01.data = BarData(barDataSet01, barDataSet02)
        val barData = BarData(barDataSet01, barDataSet02).apply {
            barWidth = 0.3f
        }
        walkingDataChart01.data = barData
        walkingDataChart01.axisLeft.apply {
            axisMaximum = 50000F
            axisMinimum = 0F
            labelCount = 5
            setDrawTopYLabelEntry(true)
            setValueFormatter { value, axis -> "" + value.toInt() }
        }
        walkingDataChart01.axisRight.apply {
            setDrawLabels(false)
            setDrawGridLines(false)
            setDrawZeroLine(false)
            setDrawTopYLabelEntry(true)
        }
        walkingDataChart01.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            labelCount = 10
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
            setDrawGridLines(false)
            setDrawAxisLine(true)
        }

        walkingDataChart01.apply {
            setDrawValueAboveBar(true)
            description.isEnabled = false
            isClickable = false
            isDragEnabled = true
            legend.textSize = 100f
            isHorizontalScrollBarEnabled = true
            setTouchEnabled(true)
            setScaleEnabled(true)
            setDrawGridBackground(false)
            setPinchZoom(true)
            setVisibleXRangeMaximum(10F)
            animateY(1200, Easing.EasingOption.Linear)
            enableScroll()
            moveViewToX(WalkingFragment.walkingParameters.size.toFloat())
        }
        walkingDataChart01.invalidate()
    }

    private fun showWalkingDataBarChart() {
        val chartData = BarData(getWalkingBarData())
        chartData.setValueTextColor(Color.BLACK)
        walkingDataChart01.data = chartData

        walkingDataChart01.axisLeft.apply {
            axisMaximum = 50000F
            axisMinimum = 0F
            labelCount = 5
            setDrawTopYLabelEntry(true)
            setValueFormatter { value, axis -> "" + value.toInt() }
        }
        walkingDataChart01.axisRight.apply {
            setDrawLabels(false)
            setDrawGridLines(false)
            setDrawZeroLine(false)
            setDrawTopYLabelEntry(true)
        }
        val labels = ArrayList<String>()
        labels.add("")
        for (wp in WalkingFragment.walkingParameters) {
            labels.add(DateFormat.format("MM/dd", wp.calendar).toString())
        }
        walkingDataChart01.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            labelCount = 10
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
            setDrawGridLines(false)
            setDrawAxisLine(true)
        }

        walkingDataChart01.apply {
            setDrawValueAboveBar(true)
            description.isEnabled = false
            isClickable = false
            isDragEnabled = true
            legend.isEnabled = false
            isHorizontalScrollBarEnabled = true
            setTouchEnabled(true)
            setScaleEnabled(true)
            setDrawGridBackground(false)
            setPinchZoom(true)
            setVisibleXRangeMaximum(10F)
            animateY(1200, Easing.EasingOption.Linear)
            enableScroll()
            moveViewToX(WalkingFragment.walkingParameters.size.toFloat())
        }
        walkingDataChart01.invalidate()
    }

    private fun getWalkingBarData(): ArrayList<IBarDataSet> {
        val entries01 = ArrayList<BarEntry>().apply {
            var i = 0
            for (wp in WalkingFragment.walkingParameters) {
                i++
                add(BarEntry(i.toFloat(), wp.steps.toFloat()))
            }
        }
        val dataSet01 = BarDataSet(entries01, "bar").apply {
            valueFormatter = IValueFormatter { value, _, _, _ -> "" + value.toInt() }
            isHighlightEnabled = false
        }
        val entries02 = ArrayList<BarEntry>().apply {
            var i = 0
            for (wp in WalkingFragment.walkingParameters) {
                i++
                add(BarEntry(i.toFloat(), wp.goal.toFloat()))
            }
        }
        val dataSet02 = BarDataSet(entries02, "bar").apply {
            valueFormatter = IValueFormatter { value, _, _, _ -> "" + value.toInt() }
            isHighlightEnabled = false
        }
        val bars = ArrayList<IBarDataSet>()
        bars.add(dataSet01)
        bars.add(dataSet02)
        return bars
    }
}

class BrainTrainingDataChartFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_fragment_user_data_brain_training, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showBrainTrainingData()
        //showBrainTrainingDataRadarChart()
    }

    private fun showBrainTrainingData() {
        val parameters = getParametersAverage()
        var text = ""
        var mil = (parameters.time).toInt() % 1000
        var sec = (parameters.time / 1000).toInt() % 60
        var min = (parameters.time / 1000 / 60).toInt() % 60
        var hou = (parameters.time / 1000 / 60 / 60).toInt() % 60
        //var time = String.format("平均プレイ時間 : %02d:%02d:%02d:%03d\n", hou, min, sec, mil)
        text += "\n"
        var time = String.format("平均プレイ時間 : %02d分 %02d秒 %03d\n", min, sec, mil)
        text += time
        text += "平均難易度 : ${parameters.difficulty}\n"
        text += "平均正答率 : ${parameters.rate * 100} %\n"
        text += "総プレイ回数 : ${parameters.play.toInt()} 回\n"
        brainTrainingDataTextView01.text = text
    }

    private fun showBrainTrainingDataRadarChart() {
        val entries01 = ArrayList<RadarEntry>()
        val parameters = getParametersAverage()
        entries01.add(RadarEntry(10f, 0f))
        entries01.add(RadarEntry(parameters.difficulty, 1f))
        entries01.add(RadarEntry(parameters.rate, 2f))
        entries01.add(RadarEntry(parameters.play, 3f))
        val radarDataSet01 = RadarDataSet(entries01, "総合評価")

        val entries02 = ArrayList<RadarEntry>()
        entries02.add(RadarEntry(7f, 0f))
        entries02.add(RadarEntry(5f, 1f))
        entries02.add(RadarEntry(3f, 2f))
        entries02.add(RadarEntry(10f, 3f))
        val radarDataSet02 = RadarDataSet(entries02, "直近10回の評価")

        val labels = ArrayList<String>()
        labels.add("time")
        labels.add("difficulty")
        labels.add("rate")
        labels.add("play")

        val dataSets = arrayListOf<RadarDataSet>(radarDataSet01, radarDataSet02)

        //val chartData = RadarData(getWalkingBarData())
        val chartData = RadarData(radarDataSet01, radarDataSet02)
        chartData.labels = labels

        chartData.setValueTextColor(Color.BLACK)
        userDataChart02.data = chartData
        userDataChart02.invalidate()
    }

    private fun getWalkingBarData(): ArrayList<IRadarDataSet> {
        val p = getParametersAverage()
        val entries = ArrayList<RadarEntry>().apply {
            /*
            add(RadarEntry(0f, p.time))
            add(RadarEntry(0f, p.difficulty))
            add(RadarEntry(0f, p.rate))
            add(RadarEntry(0f, p.play))
            */
            add(RadarEntry(10f, 0))
            add(RadarEntry(p.difficulty, 1))
            add(RadarEntry(p.rate, 2))
            add(RadarEntry(p.play, 3))
            add(RadarEntry(10f, 4))
        }
        val dataSet = RadarDataSet(entries, "bar").apply {
            valueFormatter = IValueFormatter { value, _, _, _ -> "" + value.toInt() }
            isHighlightEnabled = false
        }
        val bars = ArrayList<IRadarDataSet>()
        bars.add(dataSet)
        return bars
    }

    private fun getParametersAverage(): Parameters {
        val parameters = Parameters(0f, 0f, 0f, 0f)
        var blanks = 0
        var corrects = 0
        for (bp in BrainTrainingFragment.brainTrainingParameters) {
            parameters.play++
            parameters.time += bp.finishedCalendar.time.time - bp.startCalendar.time.time
            parameters.difficulty += bp.difficulty + 1
            blanks += bp.blank
            corrects += bp.correct
        }
        parameters.time /= parameters.play
        parameters.difficulty /= parameters.play
        parameters.rate = corrects.toFloat() / blanks.toFloat()
        Log.d("debug", "$parameters")
        return parameters
    }

    private data class Parameters(var time: Float, var difficulty: Float, var rate: Float, var play: Float){
        override fun toString(): String {
            return "$time,$difficulty,$rate,$play"
        }
    }
}

class ExtraDataFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_fragment_user_data_extra, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        showMealsRadarChart()
    }

    var foods = listOf("肉", "魚介類", "卵料理", "大豆・大豆製品", "牛乳", "緑黄色野菜", "海藻類", "いも類", "果物", "油")

    private fun showMealsRadarChart() {
        val labels = ArrayList<String>()
        val entries02 = ArrayList<RadarEntry>()
        val history = getHistory()

        for (i in 0 until foods.size) {
            labels.add(foods[i])
            entries02.add(RadarEntry(history[i], foods[i]))
        }
        val radarDataSet02 = RadarDataSet(entries02, "過去の栄養バランス").apply {
            color = Color.RED
            fillColor = Color.RED
            lineWidth = 3f
            setDrawFilled(true)
        }
        mealsDataRadarChart02.data = RadarData(radarDataSet02).apply {
            setLabels(labels)
            setValueTextSize(10f)
        }
        mealsDataRadarChart02.xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(labels)
            textSize = 15f
            labelCount = 10
            setDrawLabels(true)
            setDrawGridLines(false)
            setDrawAxisLine(true)
        }
        mealsDataRadarChart02.yAxis.apply {
            axisMinimum = 0f
            axisMaximum = 3f
            setDrawLabels(false)
        }
        mealsDataRadarChart02.apply {
            isRotationEnabled = false
            description.isEnabled = false
            legend.isEnabled = false
        }
        mealsDataRadarChart02.invalidate()
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