package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.oklb2018.frailtypreventionsupportsystem.R
import kotlinx.android.synthetic.main.layout_test.*
import kotlinx.android.synthetic.main.sub_activity_main_meals.*
import kotlinx.android.synthetic.main.sub_activity_main_meals_2.*


class MealsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_activity_main_meals_2, container, false)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mealsQuestionTextView.text = questions[cnt]
        mealsImageView.setImageResource(images[cnt])
        radioGroup10.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton1 -> answers[cnt] = 0
                R.id.radioButton2 -> answers[cnt] = 1
                R.id.radioButton3 -> answers[cnt] = 2
                R.id.radioButton4 -> answers[cnt] = 3
                else -> Log.d("debug", "this id: [$checkedId] is not in the RadioGroup")
            }
        }
        mealsNextQuestionButton.setOnClickListener {
            radioGroup10.clearCheck()
            if (cnt == 9) {
                finish()
                return@setOnClickListener
            }
            cnt++
            mealsQuestionTextView.text = questions[cnt]
            mealsImageView.setImageResource(images[cnt])
        }
    }

    private fun finish() {
        Toast.makeText(activity, "${answers.toString()}", Toast.LENGTH_LONG).show()
    }

    private fun setRadioButtons() {
        radioGroup00.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton00 -> answers[0] = 0
                R.id.radioButton01 -> answers[0] = 1
                R.id.radioButton02 -> answers[0] = 2
                R.id.radioButton03 -> answers[0] = 3
                else -> Log.d("debug", "this id: [$checkedId] is not in the RadioGroup")
            }
        }
        radioGroup01.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton10 -> answers[1] = 0
                R.id.radioButton11 -> answers[1] = 1
                R.id.radioButton12 -> answers[1] = 2
                R.id.radioButton13 -> answers[1] = 3
                else -> Log.d("debug", "this id: [$checkedId] is not in the RadioGroup")
            }
        }
        radioGroup02.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton20 -> answers[2] = 0
                R.id.radioButton21 -> answers[2] = 1
                R.id.radioButton22 -> answers[2] = 2
                R.id.radioButton23 -> answers[2] = 3
                else -> Log.d("debug", "this id: [$checkedId] is not in the RadioGroup")
            }
        }
        radioGroup03.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton30 -> answers[3] = 0
                R.id.radioButton31 -> answers[3] = 1
                R.id.radioButton32 -> answers[3] = 2
                R.id.radioButton33 -> answers[3] = 3
                else -> Log.d("debug", "this id: [$checkedId] is not in the RadioGroup")
            }
        }
        radioGroup04.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton40 -> answers[4] = 0
                R.id.radioButton41 -> answers[4] = 1
                R.id.radioButton42 -> answers[4] = 2
                R.id.radioButton43 -> answers[4] = 3
                else -> Log.d("debug", "this id: [$checkedId] is not in the RadioGroup")
            }
        }
        radioGroup05.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton50 -> answers[5] = 0
                R.id.radioButton51 -> answers[5] = 1
                R.id.radioButton52 -> answers[5] = 2
                R.id.radioButton53 -> answers[5] = 3
                else -> Log.d("debug", "this id: [$checkedId] is not in the RadioGroup")
            }
        }
        radioGroup06.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton60 -> answers[6] = 0
                R.id.radioButton61 -> answers[6] = 1
                R.id.radioButton62 -> answers[6] = 2
                R.id.radioButton63 -> answers[6] = 3
                else -> Log.d("debug", "this id: [$checkedId] is not in the RadioGroup")
            }
        }
        radioGroup07.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton70 -> answers[7] = 0
                R.id.radioButton71 -> answers[7] = 1
                R.id.radioButton72 -> answers[7] = 2
                R.id.radioButton73 -> answers[7] = 3
                else -> Log.d("debug", "this id: [$checkedId] is not in the RadioGroup")
            }
        }
        radioGroup08.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton80 -> answers[8] = 0
                R.id.radioButton81 -> answers[8] = 1
                R.id.radioButton82 -> answers[8] = 2
                R.id.radioButton83 -> answers[8] = 3
                else -> Log.d("debug", "this id: [$checkedId] is not in the RadioGroup")
            }
        }
        radioGroup09.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton90 -> answers[9] = 0
                R.id.radioButton91 -> answers[9] = 1
                R.id.radioButton92 -> answers[9] = 2
                R.id.radioButton93 -> answers[9] = 3
                else -> Log.d("debug", "this id: [$checkedId] is not in the RadioGroup")
            }
        }

        mealsDataInputFinishButton.setOnClickListener {
            Log.d("debug", "${answers.toString()}")
            Toast.makeText(activity, "${answers.toString()}", Toast.LENGTH_LONG).show()
        }
    }
}