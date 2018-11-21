package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.oklb2018.frailtypreventionsupportsystem.R
import kotlinx.android.synthetic.main.sub_activity_main_checklist.*


class ChecklistFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_activity_main_checklist, container, false)
    }

    /**
     * Fragmentの初期化はonActivityCreatedに記述する（onCreateの後に呼ばれる．初期化の最後に追加するのが様式らしい）
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCheckList()
    }

    private val questions = listOf(
        "一日中家の外には出ず，家の中で過ごすことが多いですか。",
        "2～3日に1回以上，仕事，買い物，散歩，通院などで外出しますか。",
        "家の中あるいは家の外で，趣味・楽しみ・好きでやっていることがありますか。",
        "親しくお話ができる近所の人はいますか。",
        "近所の人以外で，親しく行き来するような友達，別居家族または親戚はいますか。",
        "この一年間に転んだことがありますか。",
        "1 km ぐらいの距離を，不自由なく続けて歩くことができますか。",
        "本が読める程度に，目は普通に見えますか。眼鏡を使った状態でも構いません。",
        "家の中でよくつまずいたり，滑ったりしますか。",
        "転ぶことが怖くて外出を控えることがありますか。",
        "この一年間に入院したことがありますか。",
        "最近食欲はありますか。",
        "現在，たくあん程度のかたさの食べ物が噛めますか。",
        "この 6 か月間に 3 kg 以上の体重減少がありましたか。",
        "この 6 か月間に，以前に比べてからだの筋肉や脂肪が落ちてきたと思いますか。"
    )

    private val answers = arrayListOf<Boolean>()
    private val rightAnswers = arrayListOf<Boolean>(
        false, true, true, true, true, false, true,
        true, false, false, false, true, true, false, false
    )

    /**
     *
     */
    private fun setCheckList(currentIndex: Int = 0) {
        checklistQuestionText.text = questions[currentIndex]
        checklistButton01.setOnClickListener { _ ->
            run {
                Log.d("a", "pressed button yes")
                answers.add(true)
                if (currentIndex == questions.size - 1) checkAnswers()
                else setCheckList(currentIndex + 1)
            }
        }
        checklistButton02.setOnClickListener { view ->
            run {
                Log.d("a", "pressed button yes")
                answers.add(false)
                if (currentIndex == questions.size - 1) checkAnswers()
                else setCheckList(currentIndex + 1)
            }
        }
    }

    private fun checkAnswers() {
        var cnt = 0
        for (i in 0 until answers.size) {
            if (answers[i] != rightAnswers[i]) cnt++
        }
        checklistQuestionText.text = "score = $cnt"
    }
}