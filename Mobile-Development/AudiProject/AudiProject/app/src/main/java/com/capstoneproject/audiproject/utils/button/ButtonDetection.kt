package com.capstoneproject.audiproject.utils.button

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.capstoneproject.audiproject.R

class ButtonDetection(context: Context, view: View) {
    private val cardView: CardView = view.findViewById(R.id.cardView2)
    private val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraint_layout2)
    private var progressBar: ProgressBar = view.findViewById(R.id.progressBar2)
    private val textView: TextView = view.findViewById(R.id.tvAutismDetection)
    private val vectorScan: ImageView = view.findViewById(R.id.vectorScan)
    private val vectorArrow: ImageView = view.findViewById(R.id.vectorArrow)
    private var fadeIn: Animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)

    fun isPressed() {
        constraintLayout.setBackgroundColor(cardView.resources.getColor(R.color.button_pressed_detection))
        progressBar.visibility = View.VISIBLE
        textView.visibility = View.INVISIBLE
        vectorArrow.visibility = View.INVISIBLE
        vectorScan.visibility = View.INVISIBLE
        progressBar.animation = fadeIn
    }

    fun afterProgress() {
        constraintLayout.setBackgroundColor(cardView.resources.getColor(R.color.button_focused_detection))
        progressBar.visibility = View.INVISIBLE
        textView.visibility = View.VISIBLE
        vectorArrow.visibility = View.VISIBLE
        vectorScan.visibility = View.VISIBLE
    }
}