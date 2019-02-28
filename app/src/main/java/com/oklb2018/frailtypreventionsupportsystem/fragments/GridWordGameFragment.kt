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

public class GridWordGameFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.sub_activity_grid_word_game, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}

class GridWordGameSurfaceView : ActiveSurfaceView {

    companion object {
        private const val STATUS_INIT = 0
        private const val STATUS_STANDBY = 1
        private const val STATUS_RUN = 2
        private const val STATUS_FINISH = 3
        private const val STATUS_DESTROY = 4

        private const val TURN_FIELD = 0
        private const val TURN_LEFT = 1
        private const val TURN_RIGHT = 2
    }

    var viewStatus = STATUS_INIT

    lateinit var chips: ArrayList<ArrayList<Chip>>
    lateinit var leftChips: ArrayList<Chip>
    lateinit var rightChips: ArrayList<Chip>
    lateinit var allChips: ArrayList<Chip>
    var leftScore = 0
    var rightScore = 0

    //var chipValues = arrayListOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, -1)
    //var chipValues = arrayListOf(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, -1, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 6, 6, 7, 7, 8)
    /*
    var chipValues = arrayListOf(
        1, 1, 2, 2, 3, 3,
        4, 4, 5, 5, 6, 6,
        7, 7, 8, 1, 1, 1,
        2, 2, 3, 3, 4, 4,
        5, 5, 6, 6, 7, 7,
        8, 6, 6, 7, 7, -1)
        */

    var chipValues = arrayListOf(
        1, 1, 2, 2, 3,
        4, 4, 5, 5, 6,
        7, 7, 8, 1, 1,
        2, 2, 3, 3, 4,
        5, 5, 6, 6, -1
    )


    /*
    var words = arrayListOf(
        "あ", "い", "う", "え", "お",
        "か/が", "き/ぎ", "く/ぐ", "け/げ", "こ/ご",
        "さ/ざ", "し/じ", "す/ず", "せ/ぜ", "そ/ぞ",
        "た/だ", "ち/ぢ", "つ/づ", "て/で", "と/ど",
        "な", "に", "ぬ", "ね", "の",
        "は/ば/ぱ", "ひ/び/ぴ", "ふ/ぶ/ぷ", "へ/べ/ぺ", "ほ/ぼ/ぽ",
        "ま", "み", "む", "め", "も",
        "や", "ゆ", "よ",
        "ら", "り", "る", "れ", "ろ",
        "わ", "を", "ん"
        )
    */

    var words = arrayListOf(
        "あ", "い", "う", "え", "お",
        "か", "き", "く", "け", "こ",
        "さ", "し", "す", "せ", "そ",
        "た", "ち", "つ", "て", "と",
        "な", "に", "ぬ", "ね", "の",
        "は", "ひ", "ふ", "へ", "ほ",
        "ま", "み", "む", "め", "も",
        "や", "ゆ", "よ",
        "ら", "り", "る", "れ", "ろ",
        "わ", "を", "ん"
    )

    var gridSize = 5

    var turnOf = TURN_FIELD

    lateinit var movableChip: Chip

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)

    constructor(context: Context, surface: SurfaceView) : super(context, surface)

    override fun initialize() {
        val values = ArrayList<Int>()
        for (v in chipValues) values.add(v)
        values.shuffle()
        words.shuffle()
        chips = ArrayList<ArrayList<Chip>>()
        allChips = ArrayList()
        leftChips = ArrayList()
        rightChips = ArrayList()
        for (i in 0 until gridSize) {
            val row = ArrayList<Chip>()
            for (j in 0 until gridSize) {
                if (values[i * gridSize + j] < 0) {
                    movableChip = Chip(j, i, values[i * gridSize + j], "+")
                    row.add(movableChip)
                    allChips.add(movableChip)
                } else {
                    val c = Chip(j, i, values[i * gridSize + j], words[i * gridSize + j])
                    row.add(c)
                    allChips.add(c)
                }
            }
            chips.add(row)
        }
        val scale: Float = (height / gridSize).toFloat()
        val offsetX: Float = ((width - height) / 2).toFloat()
        for (row in chips) {
            for (c in row) {
                c.gridX = (c.x * scale + scale / 2).toFloat() + offsetX
                c.gridY = (c.y * scale + scale / 2).toFloat()
                c.r = scale / 2
            }
        }
        turnOf = TURN_LEFT
        viewStatus = STATUS_STANDBY
    }

    override fun update(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        when (viewStatus) {
            STATUS_STANDBY -> {
                viewStatus = STATUS_RUN
            }
            STATUS_RUN -> {
                updateMain()
                drawMain(canvas)
            }
            STATUS_FINISH -> {
                updateResult()
                drawResult(canvas)
            }
            STATUS_DESTROY -> {
            }
        }
    }

    private fun updateMain() {
        val scale: Float = (height / gridSize).toFloat()
        val offsetX: Float = ((width - height) / 2).toFloat()
        for (c in allChips) {
            if ((touchStatus == TOUCH_DOWN || touchStatus == TOUCH_MOVE) && c.isClickable && c.isInside(
                    touchedX,
                    touchedY
                )
            ) {
                movableChip.x = c.x
                movableChip.y = c.y
                movableChip.gridX = (c.x * scale + scale / 2).toFloat() + offsetX
                movableChip.gridY = (c.y * scale + scale / 2).toFloat()
                c.caughtSide = turnOf
                c.isClickable = false
                c.x = -1
                c.y = -1
                when (turnOf) {
                    TURN_LEFT -> {
                        leftChips.add(c.apply {
                            gridX = (leftChips.size / (allChips.size / 4)).toInt() * offsetX / 2 + offsetX / 4
                            gridY = ((leftChips.size % (allChips.size / 4) + 2) * scale / 2).toFloat()
                            r /= 2
                        })
                        leftScore += c.value
                        turnOf = TURN_RIGHT
                    }
                    TURN_RIGHT -> {
                        rightChips.add(c.apply {
                            gridX = offsetX + height +
                                    ((rightChips.size / (allChips.size / 4)).toInt() * offsetX / 2) + offsetX / 4
                            gridY = ((rightChips.size % (allChips.size / 4) + 2) * scale / 2).toFloat()
                            r /= 2
                        })
                        rightScore += c.value
                        turnOf = TURN_LEFT
                    }
                }
            }
        }
        for (row in chips) {
            for (c in row) {
                when (turnOf) {
                    TURN_LEFT -> {
                        c.isClickable = c.x == movableChip.x
                    }
                    TURN_RIGHT -> {
                        c.isClickable = c.y == movableChip.y
                    }
                }
            }
        }
        movableChip.isClickable = false
        if (leftChips.size + rightChips.size == chipValues.size - 1) {
            viewStatus = STATUS_FINISH
        }
        var existClickable = false
        for (c in allChips) {
            if (c.isClickable) existClickable = true
        }
        if (!existClickable) {
            viewStatus = STATUS_FINISH
        }
        if (leftChips.size == 6 && rightChips.size == 6) viewStatus = STATUS_FINISH
    }

    private fun drawMain(canvas: Canvas) {
        val scale: Float = (height / gridSize).toFloat()
        val offsetX: Float = ((width - height) / 2).toFloat()
        for (row in chips) {
            for (c in row) {
                var cp = Paint().apply {
                    style = Paint.Style.FILL
                    color = Color.BLACK
                }
                if (c.isClickable) {
                    cp = Paint().apply {
                        style = Paint.Style.FILL
                        color = Color.BLUE
                    }
                }
                canvas.drawCircle(c.gridX, c.gridY, c.r, cp)
                canvas.drawTextCenter(c.text, c.gridX, c.gridY,
                    Paint().apply {
                        style = Paint.Style.FILL
                        color = Color.WHITE
                        textSize = 100f
                    })
            }
        }
        canvas.drawCircle(movableChip.gridX, movableChip.gridY, movableChip.r, Paint().apply {
            style = Paint.Style.FILL
            color = Color.RED
        })
        canvas.drawTextCenter(movableChip.text, movableChip.gridX, movableChip.gridY, Paint().apply {
            style = Paint.Style.FILL
            color = Color.WHITE
            textSize = 100f
        })
        when (turnOf) {
            TURN_LEFT -> canvas.drawTextCenter("▲", offsetX / 2, height - 200f, Paint().apply {
                style = Paint.Style.FILL
                color = Color.RED
                textSize = 300f
            })
            TURN_RIGHT -> canvas.drawTextCenter(
                "▲",
                offsetX + scale * gridSize + offsetX / 2,
                height - 200f,
                Paint().apply {
                    style = Paint.Style.FILL
                    color = Color.RED
                    textSize = 300f
                })
        }
    }

    private fun updateResult() {

    }

    private fun drawResult(canvas: Canvas) {
        for (i in 0 until 6) {
            val c = leftChips[i]
            val r = width / 7
            val x = (r * (i + 0.5)).toFloat()
            val y = (height / 4).toFloat()
            canvas.drawCircle(x, y, r.toFloat() / 2, Paint().apply {
                style = Paint.Style.FILL
                color = Color.BLACK
            })
            canvas.drawTextCenter(c.text, x, y, Paint().apply {
                style = Paint.Style.FILL
                color = Color.WHITE
                textSize = 100f
            })
        }
        for (i in 0 until 6) {
            val c = rightChips[i]
            val r = width / 7
            val x = (r * (i + 1.5)).toFloat()
            val y = (height * 3 / 4).toFloat()
            canvas.drawCircle(x, y, r.toFloat() / 2, Paint().apply {
                style = Paint.Style.FILL
                color = Color.BLACK
            })
            canvas.drawTextCenter(c.text, x, y, Paint().apply {
                style = Paint.Style.FILL
                color = Color.WHITE
                textSize = 100f
            })
        }
        canvas.drawTextCenter("ひらがなを組み合わせて単語を作りましょう", (width / 2).toFloat(), (height / 2).toFloat() - 50, Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
            textSize = 80f
        })
        canvas.drawTextCenter("周囲の人と相談してもOKです", (width / 2).toFloat(), (height / 2).toFloat() + 50, Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
            textSize = 80f
        })
    }

    override fun onFinish() {}

    class Chip(
        var x: Int,
        var y: Int,
        var value: Int,
        var text: String = value.toString(),
        var gridX: Float = 0f,
        var gridY: Float = 0f,
        var r: Float = 0f,
        var caughtSide: Int = TURN_FIELD,
        var isClickable: Boolean = false
    ) {
        fun distance(cx: Float, cy: Float): Float {
            return Math.sqrt(((gridX - cx) * (gridX - cx) + (gridY - cy) * (gridY - cy)).toDouble()).toFloat()
        }

        fun isInside(cx: Float, cy: Float): Boolean {
            return distance(cx, cy) < r
        }
    }
}
