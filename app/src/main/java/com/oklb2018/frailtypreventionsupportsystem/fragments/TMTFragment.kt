package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.AttributeSet
import android.util.Log
import android.view.*
import com.oklb2018.frailtypreventionsupportsystem.R
import kotlinx.android.synthetic.main.sub_activity_main_tmt.*
import java.util.*

public class TMTFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_activity_main_tmt, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //TMTSurfaceView(activity!!, surfaceViewForTMT)

    }
}

class TMTSurfaceView : SurfaceView, SurfaceHolder.Callback, Runnable {

    companion object {
        private const val FPS = 20
        private const val FRAME: Long = (1000f / FPS).toLong()
        private const val TOUCH_INIT = 0
        private const val TOUCH_DOWN = 1
        private const val TOUCH_MOVE = 2
        private const val TOUCH_UP = 3
    }

    lateinit var surfaceHolder: SurfaceHolder
    var thread: Thread? = null

    var lastTouchedX: Float = 0f
    var lastTouchedY: Float = 0f
    var touchedX: Float = 0f
    var touchedY: Float = 0f

    var touchStatus = TOUCH_INIT

    var circles = listOf(
        Circle(100f, 100f, 100f, 1),
        Circle(300f, 200f, 100f, 2),
        Circle(500f, 300f, 100f, 3),
        Circle(700f, 400f, 100f, 4),
        Circle(900f, 600f, 100f, 5),
        Circle(1100f, 700f, 100f, 6),
        Circle(100f, 800f, 100f, 7),
        Circle(500f, 900f, 100f, 8),
        Circle(900f, 100f, 100f, 9),
        Circle(1300f, 200f, 100f, 10)
    )

    constructor(context: Context) : super(context) {
        surfaceHolder = holder
        surfaceHolder.addCallback(this)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        surfaceHolder = holder
        surfaceHolder.addCallback(this)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle) {
        surfaceHolder = holder
        surfaceHolder.addCallback(this)
    }

    constructor(context: Context, surface: SurfaceView)
            : super(context) {
        surfaceHolder = surface.holder
        surfaceHolder.addCallback(this)
    }

    override fun run() {
        var loopCounter: Long = 0
        var waitTime: Long = 0
        val starTime = System.currentTimeMillis()

        while (thread != null) {
            loopCounter++
            update()
            draw()
            waitTime = (loopCounter * FRAME) - (System.currentTimeMillis() - starTime)
            if (waitTime > 0) {
                Thread.sleep(waitTime)
            }
        }
    }

    private fun update() {

    }

    private fun draw() {
        val canvas = surfaceHolder.lockCanvas()
        canvas.drawColor(Color.WHITE)
        canvas.drawText("$touchedX, $touchedY", 1200f, 1000f, Paint().apply {
            style = Paint.Style.FILL
            color = Color.RED
            textSize = 32f
        })
        for (c in circles) {
            canvas.drawCircle(c.x, c.y, c.r, Paint().apply {
                style = Paint.Style.STROKE
                color = Color.RED
            })
            canvas.drawText(c.text, c.x - c.r / 4, c.y + c.r / 4, Paint().apply {
                style = Paint.Style.FILL
                color = Color.RED
                textSize = 100f
            })
        }
        surfaceHolder.unlockCanvasAndPost(canvas)
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        thread = Thread(this).apply { start() }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        thread = null
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        lastTouchedX = touchedX
        lastTouchedY = touchedY
        touchedX = event!!.x
        touchedY = event!!.y
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> touchStatus = TOUCH_DOWN
            MotionEvent.ACTION_MOVE -> touchStatus = TOUCH_MOVE
            MotionEvent.ACTION_UP -> touchStatus = TOUCH_UP
        }
        return true
    }

    data class Circle(var x: Float, var y: Float, var r: Float, var value: Int, var text: String = value.toString(), var isClicked: Boolean = false)
}