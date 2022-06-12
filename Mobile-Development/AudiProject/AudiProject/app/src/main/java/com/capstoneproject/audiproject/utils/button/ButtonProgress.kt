package com.capstoneproject.audiproject.utils.button

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.capstoneproject.audiproject.R

class ButtonProgress(context: Context, view: View) {

    private val cardView: CardView = view.findViewById(R.id.cardView)
    private val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraint_layout)
    private var progressBar: ProgressBar = view.findViewById(R.id.progressBar)
    private val textView: TextView = view.findViewById(R.id.textView)
    private var fadeIn: Animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)

    fun isPressed() {
        constraintLayout.setBackgroundColor(cardView.resources.getColor(R.color.button_pressed))
        progressBar.visibility = View.VISIBLE
        textView.visibility = View.INVISIBLE
        progressBar.animation = fadeIn
    }

    fun afterProgress() {
        constraintLayout.setBackgroundColor(cardView.resources.getColor(R.color.button_focused))
        progressBar.visibility = View.INVISIBLE
        textView.visibility = View.VISIBLE
    }

}