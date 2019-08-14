package com.anwesh.uiprojects.linkedsquareedgecircleview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.squareedgecircleview.SquareEdgeCircleView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SquareEdgeCircleView.create(this)
    }
}
