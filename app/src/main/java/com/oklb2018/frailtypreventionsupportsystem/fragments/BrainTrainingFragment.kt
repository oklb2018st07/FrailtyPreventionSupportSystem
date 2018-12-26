package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.app.Dialog
import android.content.Context
import android.content.IntentSender
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import com.oklb2018.frailtypreventionsupportsystem.R
import com.oklb2018.frailtypreventionsupportsystem.elements.AppManager
import com.oklb2018.frailtypreventionsupportsystem.elements.FileManager
import com.oklb2018.frailtypreventionsupportsystem.elements.toDate
import com.oklb2018.frailtypreventionsupportsystem.elements.toDateAndTime
import kotlinx.android.synthetic.main.sub_activity_main_brain_training.*
import java.util.*
import kotlin.collections.ArrayList


class BrainTrainingFragment : Fragment(), SelectAnswerDialog.Companion.OnSelectListener {

    companion object {
        private const val GAME_STATUS_INITIALIZE = 0
        private const val GAME_STATUS_MEMORIZE = 1
        private const val GAME_STATUS_REPLY = 2
        private const val GAME_STATUS_RESULT = 3

        public lateinit var brainTrainingParameters: ArrayList<BrainTrainingParameter>

        public fun readBrainTrainingParameters() {
            val parameters = ArrayList<BrainTrainingParameter>()
            val lines = FileManager().CsvReader(fileName = FileManager.brainTrainingResultFileName).readLines()
            for (i in 1 until lines.size) {
                val row = lines[i].split(",")
                val bp = BrainTrainingParameter(
                    row[0].toDateAndTime()!!,
                    row[1].toDateAndTime()!!,
                    row[2].toInt(),
                    row[3].toInt(),
                    row[4].toInt(),
                    row[5].toInt(),
                    row[6].toInt(),
                    row[7].toInt(),
                    ArrayList(),
                    ArrayList(),
                    ArrayList()
                )
                for (c in row[8]) {
                    if (c == 'E') {
                        bp.location.add(-1)
                    } else {
                        bp.location.add(c.toString().toInt())
                    }
                }
                for (c in row[9]) {
                    bp.questions.add(c.toString().toInt())
                }
                for (c in row[10]) {
                    if (c == 'E') {
                        bp.answers.add(-1)
                    } else {
                        bp.answers.add(c.toString().toInt())
                    }
                }
                parameters.add(bp)
            }
            brainTrainingParameters = parameters
            Log.d("debug", "$brainTrainingParameters")
        }
    }

    public lateinit var onReturnListener: OnReturnListener

    var height: Int = 0
    var width: Int = 0

    lateinit var buttonList: List<ImageView>
    lateinit var buttonCoverList: List<ImageView>
    lateinit var buttonMaskList: List<ImageView>

    lateinit var brainTrainingParameter: BrainTrainingParameter

    val totals = listOf(4, 4, 4, 8, 8, 8, 12, 12, 12)
    val blanks = listOf(1, 2, 3, 2, 4, 6, 3, 6, 9)
    var skinId: Int = 0
    var difficulty = 0

    var gameStatus = GAME_STATUS_INITIALIZE

    lateinit var location: ArrayList<Int>
    lateinit var questions: ArrayList<Int>
    lateinit var answers: ArrayList<Int>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.sub_activity_main_brain_training, container, false)
        root.post {
            height = root.measuredHeight
            width = root.measuredWidth
            Log.d("debug", "h: $height, w: $width")
            setComponentSize()
            initialize()
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        onReturnListener = context as OnReturnListener
    }

    private fun setComponentSize() {
        val scale = height
        Log.d("debug", "scale: $scale")
        buttonList = listOf<ImageView>(
            ImageView01, ImageView02, ImageView03, ImageView04, ImageView05, ImageView06,
            ImageView07, ImageView08, ImageView09, ImageView10, ImageView11, ImageView12
        )
        buttonCoverList = listOf<ImageView>(
            coverImageView01, coverImageView02, coverImageView03, coverImageView04, coverImageView05, coverImageView06,
            coverImageView07, coverImageView08, coverImageView09, coverImageView10, coverImageView11, coverImageView12
        )
        buttonMaskList = listOf<ImageView>(
            maskImageView01, maskImageView02, maskImageView03, maskImageView04, maskImageView05, maskImageView06,
            maskImageView07, maskImageView08, maskImageView09, maskImageView10, maskImageView11, maskImageView12
        )
        rowLayout01.layoutParams.height = scale / 3
        rowLayout01.layoutParams.width = scale / 3 * 4
        rowLayout02.layoutParams.height = scale / 3
        rowLayout02.layoutParams.width = scale / 3 * 4
        rowLayout03.layoutParams.height = scale / 3
        rowLayout03.layoutParams.width = scale / 3 * 4
        for (view in buttonList) {
            view.layoutParams.height = scale / 3
            view.layoutParams.width = scale / 3
        }
        for (view in buttonCoverList) {
            view.layoutParams.height = scale / 3
            view.layoutParams.width = scale / 3
        }
        for (view in buttonMaskList) {
            view.layoutParams.height = scale / 3
            view.layoutParams.width = scale / 3
        }
    }

    private fun initialize() {
        setParameters()
        setInitializePhase()
    }

    private fun setParameters() {
        location = ArrayList<Int>()
        questions = arrayListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
        answers = ArrayList<Int>()
        for (i in 0 until 12) {
            if (i < totals[difficulty]) {
                location.add(Random().nextInt(4))
            } else {
                location.add(-1)
            }
        }
        var b = 0
        while (b < blanks[difficulty]) {
            val index = Random().nextInt(totals[difficulty])
            if (questions[index] == 0) {
                questions[index] = 1
                b++
            }
        }
        for (i in 0 until location.size) {
            if (questions[i] == 0) answers.add(location[i]) else answers.add(-1)
        }
        brainTrainingParameter = BrainTrainingParameter(
            Calendar.getInstance(),
            Calendar.getInstance(),
            skinId,
            difficulty,
            0,
            totals[difficulty],
            blanks[difficulty],
            -1,
            location,
            questions,
            answers
        )
    }

    private fun retry() {
        val newAnswers = answers
        for (i in 0 until totals[difficulty]) {
            if (questions[i] == 1 && location[i] != answers[i]) {
                newAnswers[i] = -1
            }
        }
        brainTrainingParameter = BrainTrainingParameter(
            Calendar.getInstance(),
            Calendar.getInstance(),
            skinId,
            difficulty,
            brainTrainingParameter.retry + 1,
            totals[difficulty],
            brainTrainingParameter.blank - brainTrainingParameter.correct,
            -1,
            location,
            questions,
            answers
        )
        setInitializePhase()
    }

    private fun setInitializePhase() {
        for (view in buttonList) {
            view.setImageResource(0)
            view.clearAnimation()
        }
        for (view in buttonCoverList) {
            view.setImageResource(0)
            view.clearAnimation()
            view.setOnClickListener(null)
        }
        for (view in buttonMaskList) {
            view.setImageResource(0)
            view.clearAnimation()
        }
        gameReturnButton.text = ""
        gameGotoResultButton.text = ""
        scoreTextView.text = ""
        setMemorizePhase()
    }

    private fun setMemorizePhase() {
        gameStatus = GAME_STATUS_MEMORIZE
        for (i in 0 until totals[difficulty]) {
            val view = buttonList[i]
            view.setImageResource(ImageSkin.images[brainTrainingParameter.location[i]])
        }
        for (view in buttonCoverList) {
            view.setImageResource(0)
            view.clearAnimation()
            view.setOnClickListener(null)
        }
        for (view in buttonMaskList) {
            view.setImageResource(0)
            view.clearAnimation()
        }
        gameReturnButton.setOnClickListener {
            onReturnListener.onReturn()
        }
        gameGotoResultButton.setOnClickListener {
            setReplyPhase()
        }
        gameReturnButton.text = "戻る"
        gameGotoResultButton.text = "覚えた！"
        scoreTextView.text = ""
    }

    private fun setReplyPhase() {
        gameStatus = GAME_STATUS_REPLY
        for (i in 0 until totals[difficulty]) {
            val view = buttonCoverList[i]
            if (brainTrainingParameter.answers[i] == -1) {
                buttonList[i].setImageResource(0)
                view.setImageResource(ImageSkin.emptyAnswer)
                view.setOnClickListener {
                    Log.d("debug", "I am : $i")
                    val selectAnswerDialog = SelectAnswerDialog()
                    selectAnswerDialog.onSelectListener = this
                    selectAnswerDialog.pos = i
                    selectAnswerDialog.show(fragmentManager, "test")
                }
                val alphaAnimation = AlphaAnimation(1f, 0.5f)
                alphaAnimation.duration = 1500
                alphaAnimation.repeatCount = Animation.INFINITE
                alphaAnimation.repeatMode = Animation.REVERSE
                view.startAnimation(alphaAnimation)
            }
        }
        gameReturnButton.setOnClickListener {
            setMemorizePhase()
        }
        gameGotoResultButton.setOnClickListener {
            setResultPhase()
        }
        gameReturnButton.text = "戻る"
        gameGotoResultButton.text = "答え合わせ"
        scoreTextView.text = ""
    }

    private fun setResultPhase() {
        gameStatus = GAME_STATUS_RESULT
        brainTrainingParameter.finishedCalendar = Calendar.getInstance()
        var correct = 0
        for (i in 0 until totals[difficulty]) {
            if (questions[i] == 1) {
                if (location[i] == answers[i]) {
                    correct++
                    buttonMaskList[i].setImageResource(ImageSkin.tureAnswer)
                    val alphaAnimation = AlphaAnimation(1f, 0.5f)
                    alphaAnimation.duration = 1500
                    alphaAnimation.repeatCount = Animation.INFINITE
                    alphaAnimation.repeatMode = Animation.REVERSE
                    buttonMaskList[i].startAnimation(alphaAnimation)
                } else {
                    buttonMaskList[i].setImageResource(ImageSkin.falseAnswer)
                    buttonList[i].setImageResource(ImageSkin.images[location[i]])
                    buttonMaskList[i].startAnimation(AlphaAnimation(1f, 0.1f).apply {
                        duration = 1500
                        repeatCount = Animation.INFINITE
                        repeatMode = Animation.REVERSE
                    })
                    buttonCoverList[i].startAnimation(AlphaAnimation(1f, 0.1f).apply {
                        duration = 1500
                        repeatCount = Animation.INFINITE
                        repeatMode = Animation.REVERSE
                    })
                }
            }
        }
        scoreTextView.text = "" + correct + "/" + brainTrainingParameter.blank
        brainTrainingParameter.correct = correct
        brainTrainingParameter.finishedCalendar = Calendar.getInstance()
        brainTrainingParameters.add(brainTrainingParameter)
        parameterSave()
        Log.d("debug", "correct : $correct, blanks : ${brainTrainingParameter.blank}")
        Log.d("debug", "${brainTrainingParameter.toString()}")

        gameReturnButton.text = "設定に戻る"
        gameReturnButton.setOnClickListener {
            onReturnListener.onReturn()
        }

        if (correct == brainTrainingParameter.blank) {
            gameGotoResultButton.text = "もう一度"
            gameGotoResultButton.setOnClickListener {
                initialize()
            }
        } else {
            gameGotoResultButton.text = "再挑戦"
            gameGotoResultButton.setOnClickListener {
                retry()
            }
        }
    }

    override fun onSelected(pos: Int, id: Int) {
        Log.d("debug", "Click!! : $pos, $id")
        answers[pos] = id
        buttonCoverList[pos].setImageResource(ImageSkin.images[id])
        buttonCoverList[pos].clearAnimation()
    }

    private fun parameterSave() {
        FileManager().CsvWriter(fileName = FileManager.brainTrainingResultFileName)
            .overwriteBrainTrainingParameter(brainTrainingParameters)
    }
}

public interface OnReturnListener {
    public fun onReturn()
}

class ImageSkin {
    companion object {
        public val allImageList = arrayListOf(
            listOf(
                R.drawable.circle, R.drawable.rectangler, R.drawable.star, R.drawable.triangle
            ),
            listOf(
                R.drawable.flower_1, R.drawable.flower_2, R.drawable.flower_3, R.drawable.flower_4
            ),
            listOf(
                R.drawable.onigiri_seachicken,
                R.drawable.onigiri_yakionigiri,
                R.drawable.onigiri_takana,
                R.drawable.onigiri_yukari
            ),
            listOf(
                R.drawable.onigiri_ume,
                R.drawable.onigiri_tenmusu,
                R.drawable.onigiri_tarako,
                R.drawable.onigiri_seachicken
            )
        )

        public val titles = listOf("幾何学模様", "花々", "おにぎり", "おむすび")

        public var images = allImageList[0]

        public val tureAnswer = R.drawable.true_answer
        public val falseAnswer = R.drawable.false_answer
        public val emptyAnswer = R.drawable.empty_answer

        public fun setSkinId(id: Int) {
            images = allImageList[id]
        }
    }
}

data class BrainTrainingParameter(
    var startCalendar: Calendar,
    var finishedCalendar: Calendar,
    var skinId: Int,
    var difficulty: Int,
    var retry: Int,
    var total: Int,
    var blank: Int,
    var correct: Int,
    var location: ArrayList<Int>,
    var questions: ArrayList<Int>,
    var answers: ArrayList<Int>
) {
    override fun toString(): String {
        var str = ""
        str += DateFormat.format(FileManager.APP_DATE_AND_TIME_FORMAT, startCalendar).toString() + ","
        str += DateFormat.format(FileManager.APP_DATE_AND_TIME_FORMAT, finishedCalendar).toString() + ","
        str += "" + skinId + "," + difficulty + "," + retry + "," + total + "," + blank + "," + correct + ","
        for (c in location) str += if (c != -1) "" + c else "E"
        str += ","
        for (c in questions) str += if (c != -1) "" + c else "E"
        str += ","
        for (c in answers) str += if (c != -1) "" + c else "E"
        return str
    }
}

class SelectAnswerDialog : DialogFragment() {

    companion object {
        public interface OnSelectListener {
            public fun onSelected(pos: Int, id: Int)
        }
    }

    lateinit var onSelectListener: OnSelectListener

    var pos = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity!!)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )
        dialog.setContentView(R.layout.dialog_select_answer)
        dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        //val buttonList = listOf<ImageView>(dialogImageView1, dialogImageView2, dialogImageView3, dialogImageView4)
        val buttonList = listOf<ImageView>(
            dialog.findViewById(R.id.dialogImageView1),
            dialog.findViewById(R.id.dialogImageView2),
            dialog.findViewById(R.id.dialogImageView3),
            dialog.findViewById(R.id.dialogImageView4)
        )

        for (i in 0 until buttonList.size) {
            val view = buttonList[i]
            view.setImageResource(ImageSkin.images[i])
            Log.d("debug", "dialog $i ok")
            view.setOnClickListener {
                Log.d("debug", "dialog $i clicked!!")
                onSelectListener.onSelected(pos, i)
                dismiss()
            }
        }

        return dialog
    }
}