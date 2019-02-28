package com.oklb2018.frailtypreventionsupportsystem.fragments

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.AttributeSet
import android.util.Log
import android.view.*
import com.oklb2018.frailtypreventionsupportsystem.R
import com.oklb2018.frailtypreventionsupportsystem.elements.ActiveSurfaceView
import com.oklb2018.frailtypreventionsupportsystem.elements.AppManager
import com.oklb2018.frailtypreventionsupportsystem.elements.drawTextCenter
import java.util.*
import kotlin.collections.ArrayList

public class TMTFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_activity_main_tmt, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //TMTSurfaceView(activity!!, surfaceViewForTMT)
    }
}

class TMTSurfaceView : ActiveSurfaceView {

    companion object {
        private const val STATUS_INIT = 0
        private const val STATUS_STANDBY = 1
        private const val STATUS_RUN = 2
        private const val STATUS_FINISH = 3
        private const val STATUS_DESTROY = 4

        private const val TITLE_MESSAGE01 = "1から順に10までの数字を"
        private const val TITLE_MESSAGE02 = "順番にタッチしてください"

        private const val CIRCLE_RADIUS = 100f
    }

    var viewStatus = STATUS_INIT

    lateinit var startCalendar: Calendar
    lateinit var finishCalendar: Calendar

    lateinit var circles: ArrayList<Circle>
    var currentCircle: Circle? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

    constructor(context: Context, surface: SurfaceView) : super(context, surface)

    override fun initialize() {
        generate()
        viewStatus = STATUS_STANDBY
        startCalendar = Calendar.getInstance()
    }

    private fun generate() {
        circles = ArrayList()
        val rnd = Random()
        for (i in 1..10) {
            var circle = Circle(0f, 0f, CIRCLE_RADIUS, i)
            var isAdjusted = false
            while (!isAdjusted) {
                circle.x = rnd.nextInt((width - CIRCLE_RADIUS).toInt()) + CIRCLE_RADIUS
                circle.y = rnd.nextInt((height - CIRCLE_RADIUS).toInt()) + CIRCLE_RADIUS
                isAdjusted = true
                for (c in circles) {
                    if (circle.distance(c.x, c.y) < 200) {
                        isAdjusted = false
                    }
                }
                if (circle.x < CIRCLE_RADIUS || circle.x > width - CIRCLE_RADIUS || circle.y < CIRCLE_RADIUS || circle.y > height - CIRCLE_RADIUS) {
                    isAdjusted = false
                }
            }
            circles.add(circle)
        }
        for (i in 0 until circles.size - 1) {
            circles[i].nextCircle = circles[i + 1]
            Log.d("debug", circles[i].nextCircle!!.text)
        }
        currentCircle = circles[0]
    }

    override fun update(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        when (viewStatus) {
            STATUS_STANDBY -> {
                updateStandby(canvas)
            }
            STATUS_RUN -> {
                updateMain(canvas)
            }
            STATUS_FINISH -> {
                drawResult(canvas)
            }
            STATUS_DESTROY -> {

            }
        }
    }

    private fun updateStandby(canvas: Canvas) {
        canvas.drawTextCenter(TITLE_MESSAGE01, (width / 2).toFloat(), (height / 2).toFloat() - 280f, Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
            textSize = 120f
        })
        canvas.drawTextCenter(TITLE_MESSAGE02, (width / 2).toFloat(), (height / 2).toFloat() - 150f, Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
            textSize = 120f
        })

        canvas.drawTextCenter("${((100L - loopCounter) / FPS) + 1}", (width / 2).toFloat(), (height / 2).toFloat() + 150f, Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
            textSize = 200f
        })
        if (loopCounter >= 100L) {
            viewStatus = STATUS_RUN
            startCalendar = Calendar.getInstance()
        }
    }

    private fun updateMain(canvas: Canvas) {
        if ((touchStatus == TOUCH_DOWN || touchStatus == TOUCH_MOVE) && !currentCircle!!.isClicked && currentCircle!!.isInside(touchedX, touchedY)) {
            Log.d("debug", "num: ${currentCircle!!.value} is clicked!")
            currentCircle!!.isClicked = true
            if (currentCircle!!.nextCircle == null){
                finishCalendar = Calendar.getInstance()
                viewStatus = STATUS_FINISH
                return
            }
            currentCircle = currentCircle!!.nextCircle
        }
        drawMain(canvas)
    }

    private fun drawMain(canvas: Canvas) {
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textSize = 100f
        }
        for (c in circles) {
            if (c.isClicked) {
                canvas.drawCircle(c.x, c.y, c.r, Paint().apply {
                    style = Paint.Style.STROKE
                    strokeWidth = 3f
                    color = Color.BLACK
                })
                textPaint.color = Color.BLACK
                canvas.drawTextCenter(c.text, c.x, c.y, textPaint)
            } else {
                canvas.drawCircle(c.x, c.y, c.r, Paint().apply {
                    style = Paint.Style.FILL
                    color = Color.BLACK
                })
                textPaint.color = Color.WHITE
                canvas.drawTextCenter(c.text, c.x, c.y, textPaint)
            }
        }
    }

    private fun drawResult(canvas: Canvas) {
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.BLACK
            textSize = 100f
        }
        canvas.drawTextCenter(AppManager.deffDateString(finishCalendar, startCalendar), (width / 2).toFloat(), (height / 2).toFloat(), textPaint)
    }

    override fun onFinish() {
        Log.d("debug", "TMTSurfaceView is finished")
    }

    class Circle(
        var x: Float,
        var y: Float,
        var r: Float,
        var value: Int,
        var text: String = value.toString(),
        var isClicked: Boolean = false,
        var nextCircle: Circle? = null
    ) {
        fun distance(cx: Float, cy: Float): Float {
            return Math.sqrt(((x - cx) * (x - cx) + (y - cy) * (y - cy)).toDouble()).toFloat()
        }

        fun isInside(cx: Float, cy: Float): Boolean {
            return distance(cx, cy) < r
        }
    }
}
