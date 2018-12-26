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


class UserDataFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_activity_main_user_data, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var string: String = ""
        for (p in WalkingFragment.walkingParameters) {
            string += p.toString() + "\n"
        }

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
            showExtraData()
        }
    }

    private fun showBrainTrainingDataChart() {

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
        showWalkingDataBarChart()
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
        val entries = ArrayList<BarEntry>().apply {
            var i = 0
            for (wp in WalkingFragment.walkingParameters) {
                i++
                add(BarEntry(i.toFloat(), wp.steps.toFloat()))
            }
        }

        val dataSet = BarDataSet(entries, "bar").apply {
            valueFormatter = IValueFormatter { value, _, _, _ -> "" + value.toInt() }
            isHighlightEnabled = false
        }
        val bars = ArrayList<IBarDataSet>()
        bars.add(dataSet)
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
        var time = String.format("平均プレイ時間 : %02d:%02d:%02d:%03d\n", hou, min, sec, mil)
        text += "平均プレイ時間 : ${parameters.time}\n"
        text += "平均プレイ時間 : $hou:$min:$sec: \n"
        text += time
        text += "平均難易度 : ${parameters.difficulty}\n"
        text += "平均正答率 : ${parameters.rate}\n"
        text += "総プレイ回数 : ${parameters.play}\n"
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