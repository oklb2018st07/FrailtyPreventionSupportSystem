package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import com.oklb2018.frailtypreventionsupportsystem.R
import com.oklb2018.frailtypreventionsupportsystem.elements.ActiveSurfaceView
import com.oklb2018.frailtypreventionsupportsystem.elements.drawTextCenter
import kotlinx.android.synthetic.main.idea_communication_flip.*
import kotlinx.android.synthetic.main.sub_activity_main_idea_communication.*

public class IdeaCommunicationFragment : Fragment(), OnStandbyListener {

    private val contents = arrayListOf(
        "好きな食べ物について\n話しあってみましょう！",
        "冬の楽しみについて\n話しあってみましょう！",
        "近所の名所について\n話しあってみましょう！",
        "今日の朝の出来事について\n話しあってみましょう！",
        "昨日の夜の出来事について\n話しあってみましょう！",
        "最近見たテレビ番組について\n話しあってみましょう！")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //return inflater.inflate(R.layout.sub_activity_main_idea_communication, container, false)
        return inflater.inflate(R.layout.idea_communication_flip, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        contents.shuffle()
        iDeaFlipTextView01.text = contents[0]
        //ideaCommunicationSurfaceView01.onStandbyListener = this

        nextIdeaFlipButton.setOnClickListener { _ ->
            contents.shuffle()
            iDeaFlipTextView01.text = contents[0]
        }
    }

    override fun onReady() {
        ideaCommunicationParent.removeAllViews()
        layoutInflater.inflate(R.layout.idea_communication_flip, ideaCommunicationParent)
        contents.shuffle()
        iDeaFlipTextView01.text = contents[0]
    }
}

class IdeaCommunicationSurfaceView : ActiveSurfaceView {

    companion object {
        private const val STATUS_INIT = 0
        private const val STATUS_STANDBY = 1
        private const val STATUS_RUN = 2
        private const val STATUS_FINISH = 3
        private const val STATUS_DESTROY = 4

        private const val TITLE_MESSAGE01 = "次の話題について"
        private const val TITLE_MESSAGE02 = "皆さんで話しあってみましょう！！"
    }

    var onStandbyListener: OnStandbyListener? = null

    var viewStatus = STATUS_INIT

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

    constructor(context: Context, surface: SurfaceView) : super(context, surface)

    override fun initialize() {
        viewStatus = STATUS_STANDBY
    }

    override fun update(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        canvas.drawTextCenter(TITLE_MESSAGE01, (width / 2).toFloat(), (height / 2).toFloat() - 280f, Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
            textSize = 100f
        })
        canvas.drawTextCenter(TITLE_MESSAGE02, (width / 2).toFloat(), (height / 2).toFloat() - 150f, Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
            textSize = 100f
        })

        canvas.drawTextCenter(
            "${((100L - loopCounter) / FPS) + 1}",
            (width / 2).toFloat(),
            (height / 2).toFloat() + 150f,
            Paint().apply {
                style = Paint.Style.FILL
                color = Color.BLACK
                textSize = 200f
            })
        if (loopCounter >= 100L) destroy()
    }

    override fun onFinish() {
        Log.d("debug", "IdeaSurfaceView is finished")
        if (thread == null) return
        handler.post {
            onStandbyListener!!.onReady()
        }
    }
}

interface OnStandbyListener {
    public fun onReady()
}