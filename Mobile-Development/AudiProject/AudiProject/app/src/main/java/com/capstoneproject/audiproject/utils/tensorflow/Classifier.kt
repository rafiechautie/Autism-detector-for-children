package com.capstoneproject.audiproject.utils.tensorflow

import android.graphics.Bitmap
import java.util.*

interface Classifier {

    class Recognition(val title: String?, val confidence: Float?) {

        override fun toString(): String {
            var resultString = ""
            if (title != null) {
                resultString += "$title "
            }
            if (confidence != null) {
                resultString += String.format(
                    Locale.getDefault(), "(%.1f%%) ", confidence * 100.0f
                )
            }
            return resultString.trim { it <= ' ' }
        }
    }

    fun recognizeImage(bitmap: Bitmap?): List<Recognition?>?
    fun close()
}