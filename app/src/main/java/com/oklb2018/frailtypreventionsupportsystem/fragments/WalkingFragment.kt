package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.oklb2018.frailtypreventionsupportsystem.R
import com.oklb2018.frailtypreventionsupportsystem.elements.AppManager
import com.oklb2018.frailtypreventionsupportsystem.elements.FileManager
import com.oklb2018.frailtypreventionsupportsystem.elements.toDate
import kotlinx.android.synthetic.main.sub_activity_main_walking.*
import java.util.*
import kotlin.collections.ArrayList

class WalkingFragment : Fragment() {

    companion object {
        public lateinit var walkingParameters: ArrayList<WalkingParameter>

        public fun readWalkingParameters() {
            walkingParameters = createWalkingParameters(FileManager().CsvReader(fileName = FileManager.walkingParameterFileName).readLines())
        }

        private fun createWalkingParameters(strings: ArrayList<String>): ArrayList<WalkingParameter> {
            val parameters: ArrayList<WalkingParameter> = arrayListOf()
            val dates = AppManager.generateCalendars()
            for (d in dates) {
                val dateStr = DateFormat.format(FileManager.APP_DATE_FORMAT, d) as String
                parameters.add(WalkingParameter(d, dateStr, 0, 0))
            }
            for (i in 1 until strings.size) {
                val row = strings[i].split(",")
                val c = row[0].toDate()!!
                val index = AppManager.deffDate(c)
                parameters[index].apply {
                    calendar = c
                    dateStr = row[0]
                    goal = Integer.parseInt(row[1])
                    steps = Integer.parseInt(row[2])
                }
            }
            return parameters
        }

        public fun getDatesToString(): ArrayList<String> {
            val labels = ArrayList<String>()
            for (wp in WalkingFragment.walkingParameters) {
                labels.add(wp.dateStr)
            }
            return labels
        }

        public fun getStepsMaximum(): Int{
            var n = 0
            for (wp in walkingParameters) {
                if (n < wp.steps) n = wp.steps
            }
            return n
        }
    }

    private lateinit var currentWalkingParameter: WalkingParameter

    private var mode: Int = 1

    private var stepCount: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_activity_main_walking, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        currentWalkingParameter = walkingParameters[walkingParameters.size - 1]
        setComponents()
        stepCountUpdate()
    }

    private fun setComponents() {
        goalStepsTextView.text = "目標歩数 : " + currentWalkingParameter.goal
        goalStepsTextView.setBackgroundColor(Color.CYAN)
        myStepsTextView.text = "達成歩数 : " + currentWalkingParameter.steps
        myStepsTextView.setBackgroundColor(Color.WHITE)
        walkingSelectButton01.setOnClickListener { _ ->
            mode = 1
            goalStepsTextView.setBackgroundColor(Color.CYAN)
            myStepsTextView.setBackgroundColor(Color.WHITE)
            stepCountUpdate()
        }
        walkingSelectButton02.setOnClickListener { _ ->
            mode = 2
            goalStepsTextView.setBackgroundColor(Color.WHITE)
            myStepsTextView.setBackgroundColor(Color.CYAN)
            stepCountUpdate()
        }
        walkingInputButton00.setOnClickListener { _ ->
            stepCount = stepCount * 10 + 0
            stepCountUpdate()
        }
        walkingInputButton01.setOnClickListener { _ ->
            stepCount = stepCount * 10 + 1
            stepCountUpdate()
        }
        walkingInputButton02.setOnClickListener { _ ->
            stepCount = stepCount * 10 + 2
            stepCountUpdate()
        }
        walkingInputButton03.setOnClickListener { _ ->
            stepCount = stepCount * 10 + 3
            stepCountUpdate()
        }
        walkingInputButton04.setOnClickListener { _ ->
            stepCount = stepCount * 10 + 4
            stepCountUpdate()
        }
        walkingInputButton05.setOnClickListener { _ ->
            stepCount = stepCount * 10 + 5
            stepCountUpdate()
        }
        walkingInputButton06.setOnClickListener { _ ->
            stepCount = stepCount * 10 + 6
            stepCountUpdate()
        }
        walkingInputButton07.setOnClickListener { _ ->
            stepCount = stepCount * 10 + 7
            stepCountUpdate()
        }
        walkingInputButton08.setOnClickListener { _ ->
            stepCount = stepCount * 10 + 8
            stepCountUpdate()
        }
        walkingInputButton09.setOnClickListener { _ ->
            stepCount = stepCount * 10 + 9
            stepCountUpdate()
        }
        walkingInputButtonClear.setOnClickListener { _ ->
            stepCount = 0
            stepCountUpdate()
        }
        walkingInputButtonDecide.setOnClickListener { _ ->
            stepCountSave()
        }
        walkingSaveButton.setOnClickListener { _ ->
            parameterSave()
        }
    }

    private fun stepCountUpdate() {
        when (mode) {
            1 -> {
                stepCounterTextView.text = "目標歩数 : $stepCount"
            }
            2 -> {
                stepCounterTextView.text = "達成歩数 : $stepCount"
            }
        }
    }

    private fun stepCountSave() {
        when (mode) {
            1 -> {
                currentWalkingParameter.goal = stepCount
                goalStepsTextView.text = "目標歩数 : " + currentWalkingParameter.goal
                goalStepsTextView.setBackgroundColor(Color.WHITE)
                myStepsTextView.setBackgroundColor(Color.CYAN)
                Toast.makeText(context, "目標歩数を設定しました", Toast.LENGTH_LONG).show()
                mode = 2
            }
            2 -> {
                currentWalkingParameter.steps = stepCount
                myStepsTextView.text = "達成歩数 : " + currentWalkingParameter.steps
                goalStepsTextView.setBackgroundColor(Color.WHITE)
                myStepsTextView.setBackgroundColor(Color.WHITE)
                Toast.makeText(context, "今日歩いた歩数が反映されました", Toast.LENGTH_LONG).show()
            }
        }
        stepCount = 0
        stepCountUpdate()
    }

    private fun parameterSave() {
        FileManager().CsvWriter(fileName = FileManager.walkingParameterFileName)
            .overwriteWalkingParameter(walkingParameters)
        Toast.makeText(context, "記録完了です．お疲れ様でした！", Toast.LENGTH_LONG).show()
    }
}

public data class WalkingParameter(
    var calendar: Calendar,
    var dateStr: String,
    var goal: Int,
    var steps: Int
) {
    override fun toString(): String {
        return dateStr + ",$goal,$steps"
    }
}
