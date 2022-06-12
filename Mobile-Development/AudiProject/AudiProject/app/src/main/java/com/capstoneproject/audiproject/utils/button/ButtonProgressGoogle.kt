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

class ButtonProgressGoogle(context: Context, view: View) {

    private val cardView: CardView = view.findViewById(R.id.cardView1)
    private val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraint_layout1)
    private var progressBar: ProgressBar = view.findViewById(R.id.progressBar1)
    private val textView: TextView = view.findViewById(R.id.textView1)
    private val imgView: ImageView = view.findViewById(R.id.logoGoogle1)
    private var fadeIn: Animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)

    fun isPressed() {
        constraintLayout.setBackgroundColor(cardView.resources.getColor(R.color.button_pressed_google))
        progressBar.visibility = View.VISIBLE
        textView.visibility = View.INVISIBLE
        imgView.visibility = View.INVISIBLE
        progressBar.animation = fadeIn
    }

    fun afterProgress() {
        constraintLayout.setBackgroundColor(cardView.resources.getColor(R.color.button_focused_google))
        progressBar.visibility = View.INVISIBLE
        textView.visibility = View.VISIBLE
        imgView.visibility = View.VISIBLE
    }

}