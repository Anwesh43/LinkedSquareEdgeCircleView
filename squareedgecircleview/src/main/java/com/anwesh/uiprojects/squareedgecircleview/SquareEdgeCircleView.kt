package com.anwesh.uiprojects.squareedgecircleview

/**
 * Created by anweshmishra on 14/08/19.
 */

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color

val nodes : Int = 5
val circles : Int = 4
val scGap : Float = 0.05f
val scDiv : Double = 0.51
val strokeFactor : Int = 90
val sizeFactor : Float = 2.9f
val foreColor : Int = Color.parseColor("#311B92")
val backColor : Int = Color.parseColor("#BDBDBD")
val rFactor : Float = 4f
val sqFactor : Float = 2f

fun Int.inverse() : Float = 1f / this
fun Float.scaleFactor() : Float = Math.floor(this / scDiv).toFloat()
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n
fun Float.mirrorValue(a : Int, b : Int) : Float {
    val k : Float = scaleFactor()
    return (1 - k) * a.inverse() + k * b.inverse()
}
fun Float.updateValue(dir : Float, a : Int, b : Int) : Float = mirrorValue(a, b) * dir * scGap

fun Canvas.drawEdgeCircle(i : Int, scale : Float, r : Float, position : Float, paint : Paint) {
    val updatingPoisition : Float = position * scale.divideScale(i, circles)
    save()
    rotate(90f * i)
    drawCircle(updatingPoisition, updatingPoisition, r, paint)
    restore()
}

fun Canvas.drawSquareEdgeCircle(size : Float, scale : Float, paint : Paint) {
    val sqSize : Float = size / sqFactor
    val r : Float = size / rFactor
    drawRect(-sqSize / 2, -sqSize / 2, sqSize / 2, sqSize / 2, paint)
    for (j in 0..(circles - 1)) {
        drawEdgeCircle(j, scale, r, sqSize / 2, paint)
    }
}

fun Canvas.drawSECNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = w / (nodes + 1)
    val size : Float = gap / sizeFactor
    paint.color = foreColor
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    paint.style = Paint.Style.STROKE
    save()
    translate(gap * (i + 1), h / 2)
    rotate(90f * scale.divideScale(1, 2))
    drawSquareEdgeCircle(size, scale.divideScale(0, 2), paint)
    restore()
}

class SquareEdgeCircleView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scale.updateValue(dir, circles, 1)
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}