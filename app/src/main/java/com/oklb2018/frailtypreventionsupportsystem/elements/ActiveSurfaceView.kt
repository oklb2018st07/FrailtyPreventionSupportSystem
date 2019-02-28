package com.oklb2018.frailtypreventionsupportsystem.elements

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

public abstract class ActiveSurfaceView: SurfaceView, SurfaceHolder.Callback, Runnable {

    companion object {
        const val TOUCH_INIT = 0
        const val TOUCH_DOWN = 1
        const val TOUCH_MOVE = 2
        const val TOUCH_UP = 3
    }

    protected var FPS = 20
    protected var FRAME: Long = (1000f / FPS).toLong()

    var surfaceHolder: SurfaceHolder

    var thread: Thread? = null

    var lastTouchedX: Float = 0f
    var lastTouchedY: Float = 0f
    var touchedX: Float = 0f
    var touchedY: Float = 0f

    var touchBufferX: Float = 0f
    var touchBufferY: Float = 0f

    var touchStatus = TOUCH_INIT
    var touchStatusBuffer = TOUCH_INIT

    var loopCounter: Long = 0L

    var isDestoryEnable = false

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
        loopCounter = 0L
        initialize()
        val startTime = System.currentTimeMillis()
        while (thread != null && !isDestoryEnable) {
            loopCounter++
            loopSetting()
            val canvas = surfaceHolder.lockCanvas() ?: break
            update(canvas)
            surfaceHolder.unlockCanvasAndPost(canvas)
            sleep(startTime)
        }
        onFinish()
    }

    private fun loopSetting() {
        lastTouchedX = touchedX
        lastTouchedY = touchedY
        touchedX = touchBufferX
        touchedY = touchBufferY
        touchStatus = touchStatusBuffer
    }

    abstract fun initialize()

    abstract fun update(canvas: Canvas)

    abstract fun onFinish()

    private fun sleep(startTime: Long) {
        val waitTime = (loopCounter * FRAME) - (System.currentTimeMillis() - startTime)
        if (waitTime > 0) {
            Thread.sleep(waitTime)
        }
    }

    public fun setFrameRate(fps: Int) {
        FPS = fps
        FRAME = (1000f / FPS).toLong()
    }

    public fun destroy() {
        isDestoryEnable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        Log.d("debug", "Create")
        thread = Thread(this).apply { start() }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        thread = null
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        touchBufferX = event!!.x
        touchBufferY = event!!.y
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> touchStatusBuffer = TOUCH_DOWN
            MotionEvent.ACTION_MOVE -> touchStatusBuffer = TOUCH_MOVE
            MotionEvent.ACTION_UP -> touchStatusBuffer = TOUCH_UP
        }
        return true
    }

}

public fun Canvas.drawTextCenter(text: String, x: Float, y: Float, paint:Paint) {
    val fontMetrics = paint.getFontMetrics()
    val baseX = x - paint.measureText(text) / 2
    val baseY = y - (fontMetrics.ascent + fontMetrics.descent) / 2
    drawText(text, baseX, baseY, paint)
}