package com.capstoneproject.audiproject.utils.tensorflow

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.experimental.and

class TensorflowImageClassifier: Classifier {

    private val MAX_RESULTS = 3
    private val BATCH_SIZE = 1
    private val PIXEL_SIZE = 3
    private val THRESHOLD = 0.5f

    private var interpreter: Interpreter? = null
    private var inputSize = 0
    private var labelList: List<String>? = null



    override fun recognizeImage(bitmap: Bitmap?): List<Classifier.Recognition?>? {
        val byteBuffer: ByteBuffer = convertBitmapToByteBuffer(bitmap!!)!!
        val result = Array(1) {
            ByteArray(
                labelList!!.size
            )
        }
        interpreter!!.run(byteBuffer, result)
        return getSortedResult(result)
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    @Throws(IOException::class)
    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer? {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    @Throws(IOException::class)
    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String>? {
        val labelList: MutableList<String> = ArrayList()
        val reader = BufferedReader(InputStreamReader(assetManager.open(labelPath)))
        var line: String
        while (reader.readLine().also { line = it } != null) {
            labelList.add(line)
        }
        reader.close()
        return labelList
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer? {
        val byteBuffer =
            ByteBuffer.allocateDirect(BATCH_SIZE * inputSize * inputSize * PIXEL_SIZE)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(inputSize * inputSize)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val `val` = intValues[pixel++]
                byteBuffer.put((`val` shr 16 and 0xFF).toByte())
                byteBuffer.put((`val` shr 8 and 0xFF).toByte())
                byteBuffer.put((`val` and 0xFF).toByte())
            }
        }
        return byteBuffer
    }

    @SuppressLint("DefaultLocale")
    private fun getSortedResult(labelProbArray: Array<ByteArray>): List<Classifier.Recognition>? {
        val pq: PriorityQueue<Classifier.Recognition> = PriorityQueue<Classifier.Recognition>(
            MAX_RESULTS
        ) { lhs: Classifier.Recognition, rhs: Classifier.Recognition ->
            (rhs.confidence!!).compareTo(lhs.confidence!!)
        }
        for (i in labelList!!.indices) {
            val confidence = (labelProbArray[0][i] and 0xff.toByte()) / 255.0f
            if (confidence > THRESHOLD) {
                pq.add(
                    Classifier.Recognition(
                        if (labelList!!.size > i) labelList!![i] else "unknown",
                        confidence
                    )
                )
            }
        }
        val recognitions: ArrayList<Classifier.Recognition> = ArrayList<Classifier.Recognition>()
        val recognitionsSize = pq.size.coerceAtMost(MAX_RESULTS)
        for (i in 0 until recognitionsSize) {
            recognitions.add(pq.poll())
        }
        return recognitions
    }

    companion object {
        @Throws(IOException::class)
        fun create(
            assetManager: AssetManager?,
            modelPath: String?,
            labelPath: String?,
            inputSize: Int
        ): Classifier {
            val classifier = TensorflowImageClassifier()
            classifier.interpreter = Interpreter(classifier.loadModelFile(assetManager!!, modelPath!!)!!)
            classifier.labelList = classifier.loadLabelList(assetManager, labelPath!!)
            classifier.inputSize = inputSize
            return classifier
        }
    }
}